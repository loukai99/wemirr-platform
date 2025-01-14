/*
 * Copyright (c) 2023 WEMIRR-PLATFORM Authors. All Rights Reserved.
 *
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wemirr.framework.boot.log.configuration;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.extra.servlet.JakartaServletUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.ttl.TransmittableThreadLocal;
import com.wemirr.framework.boot.RegionUtils;
import com.wemirr.framework.boot.log.AccessLogInfo;
import com.wemirr.framework.boot.log.AccessLogUtil;
import com.wemirr.framework.boot.log.event.AccessLogEvent;
import com.wemirr.framework.commons.annotation.log.AccessLog;
import com.wemirr.framework.commons.entity.Result;
import com.wemirr.framework.commons.exception.CheckedException;
import com.wemirr.framework.commons.security.AuthenticationContext;
import com.wemirr.framework.db.properties.DatabaseProperties;
import com.wemirr.framework.db.properties.MultiTenantType;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * 操作日志使用spring event异步入库
 *
 * @author Levin
 * @since 2019-07-01 15:15
 */
@Aspect
public class AccessLogAspect {
    
    private static final Logger logger = LoggerFactory.getLogger(AccessLogAspect.class);
    
    private static final int MAX_LENGTH = 65535;
    private static final TransmittableThreadLocal<AccessLogInfo> THREAD_LOCAL = new TransmittableThreadLocal<>();
    private static final String USER_AGENT = "User-Agent";
    @Resource
    private ApplicationContext applicationContext;
    @Resource
    private DatabaseProperties databaseProperties;
    
    /**
     * 切面
     */
    @Pointcut("execution(public * com.wemirr..*.*(..)) && @annotation(com.wemirr.framework.commons.annotation.log.AccessLog)")
    public void accessLogAspect() {
        
    }
    
    private AccessLogInfo get() {
        return Optional.ofNullable(THREAD_LOCAL.get()).orElseGet(AccessLogInfo::new);
    }
    
    private void tryCatch(Consumer<String> consumer) {
        try {
            consumer.accept("");
        } catch (Exception e) {
            logger.warn("记录操作日志异常", e);
            THREAD_LOCAL.remove();
        }
    }
    
    /**
     * 返回通知
     *
     * @param ret ret
     */
    @AfterReturning(returning = "ret", pointcut = "accessLogAspect()")
    public void doAfterReturning(JoinPoint joinPoint, Object ret) {
        tryCatch((ex) -> {
            AccessLog annotation = AccessLogUtil.getTargetAnnotation(joinPoint);
            if (annotation == null) {
                return;
            }
            AccessLogInfo log = get();
            log.setStartTime(Instant.now());
            if (ret instanceof Result) {
                Result<?> result = Convert.convert(Result.class, ret);
                if (result == null) {
                    if (annotation.response()) {
                        log.setResponse(getText(JSON.toJSONString(ret)));
                    }
                } else {
                    if (!result.isSuccessful()) {
                        log.setMessage(result.getMessage());
                    }
                    if (annotation.response()) {
                        log.setResponse(result.toString());
                    }
                }
            } else {
                if (annotation.response()) {
                    log.setResponse(getText(ret == null ? "" : JSON.toJSONString(ret)));
                }
            }
            publishEvent(log);
        });
        
    }
    
    private void publishEvent(AccessLogInfo log) {
        log.setFinishTime(Instant.now());
        log.setConsumingTime(log.getStartTime().until(log.getFinishTime(), ChronoUnit.MILLIS));
        applicationContext.publishEvent(new AccessLogEvent(log));
        THREAD_LOCAL.remove();
    }
    
    /**
     * 异常通知
     */
    @AfterThrowing(pointcut = "accessLogAspect()", throwing = "e")
    public void doAfterThrowable(JoinPoint joinPoint, Throwable e) {
        tryCatch((ex) -> {
            AccessLog accessLogAnnotation = AccessLogUtil.getTargetAnnotation(joinPoint);
            if (accessLogAnnotation == null) {
                return;
            }
            AccessLogInfo log = get();
            // 遇到错误时，请求参数若为空，则记录
            HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
            String strArgs = getArgs(accessLogAnnotation, joinPoint.getArgs(), request);
            log.setRequest(getText(strArgs));
            if (e.getCause() instanceof CheckedException) {
                log.setMessage(e.getLocalizedMessage());
            } else {
                log.setMessage(ExceptionUtil.stacktraceToString(e, MAX_LENGTH));
            }
            log.setResponse(e.getMessage());
            publishEvent(log);
        });
    }
    
    /**
     * 截取指定长度的字符串
     *
     * @param val val
     * @return String
     */
    private String getText(String val) {
        if (val != null && val.length() > WARNING_LENGTH) {
            logger.warn("val length > 65535,响应内容过长请避免日志存储到数据库中,本次长度 - {}", val.length());
        }
        return val;
    }
    
    private static final int WARNING_LENGTH = 65535;
    
    @Before(value = "accessLogAspect()")
    public void recordLog(JoinPoint joinPoint) {
        tryCatch((val) -> {
            AccessLog annotation = AccessLogUtil.getTargetAnnotation(joinPoint);
            if (annotation == null) {
                return;
            }
            AccessLogInfo log = get();
            final AuthenticationContext context = SpringUtil.getBean(AuthenticationContext.class);
            if (context != null) {
                log.setTenantId(context.tenantId());
                log.setCreatedBy(context.userId());
                log.setCreatedName(context.realName());
            }
            log.setDescription(annotation.description());
            String action = joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName();
            log.setAction(action);
            HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
            String strArgs = getArgs(annotation, joinPoint.getArgs(), request);
            log.setRequest(getText(strArgs));
            final DatabaseProperties.MultiTenant multiTenant = databaseProperties.getMultiTenant();
            if (multiTenant.getType() == MultiTenantType.DATASOURCE) {
                String tenantCode = request.getHeader(multiTenant.getTenantCodeColumn());
                if (StringUtils.equals(multiTenant.getSuperTenantCode(), tenantCode)) {
                    log.setDsKey(multiTenant.getDefaultDsName());
                } else {
                    log.setDsKey(multiTenant.getDsPrefix() + tenantCode);
                }
            }
            final String ip = JakartaServletUtil.getClientIP(request);
            log.setIp(ip);
            log.setLocation(RegionUtils.getRegion(ip));
            log.setUri(URLUtil.getPath(request.getRequestURI()));
            log.setHttpMethod(request.getMethod());
            final UserAgent userAgent = UserAgentUtil.parse(request.getHeader(USER_AGENT));
            log.setEngine(userAgent.getEngine().getName());
            log.setOs(userAgent.getOs().getName());
            log.setPlatform(userAgent.getPlatform().getName());
            log.setBrowser(userAgent.getBrowser().getName());
            log.setStartTime(Instant.now());
            THREAD_LOCAL.set(log);
        });
    }
    
    private String getArgs(AccessLog accessLogAnnotation, Object[] args, HttpServletRequest request) {
        String strArgs = "";
        if (!accessLogAnnotation.request()) {
            return strArgs;
        }
        try {
            if (!request.getContentType().contains(MediaType.MULTIPART_FORM_DATA_VALUE)) {
                strArgs = JSONObject.toJSONString(args);
            }
        } catch (Exception e) {
            try {
                strArgs = Arrays.toString(args);
            } catch (Exception ex) {
                logger.warn("解析参数异常", ex);
            }
        }
        return strArgs;
    }
    
}
