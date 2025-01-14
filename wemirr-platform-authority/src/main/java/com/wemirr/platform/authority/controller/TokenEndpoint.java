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

package com.wemirr.platform.authority.controller;

import com.wemirr.framework.commons.exception.CheckedException;
import com.wemirr.framework.commons.security.AuthenticationContext;
import com.wemirr.framework.security.domain.UserInfoDetails;
import com.wemirr.platform.authority.domain.common.req.ChangePasswordReq;
import com.wemirr.platform.authority.domain.common.req.ChangeUserInfoReq;
import com.wemirr.platform.authority.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Objects;

/**
 * @author Levin
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping
@Tag(name = "Token管理", description = "Token管理")
public class TokenEndpoint {
    
    private final AuthenticationContext authenticationContext;
    private final UserService userService;
    private final OAuth2AuthorizationService oAuth2AuthorizationService;
    
    @GetMapping("/oauth2/check_token")
    public Authentication checkToken() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
    
    @GetMapping("/oauth2/userinfo")
    public Object userinfo(Principal principal) {
        // 账号密码模式登陆
        if (principal instanceof UsernamePasswordAuthenticationToken token) {
            if (token.getPrincipal()instanceof UserInfoDetails user) {
                return user;
            }
        }
        authenticationContext.tenantId();
        if (principal instanceof BearerTokenAuthentication token) {
            if (token.getPrincipal()instanceof UserInfoDetails user) {
                return user;
            }
        }
        if (principal instanceof JwtAuthenticationToken token) {
            final String tokenValue = token.getToken().getTokenValue();
            final OAuth2Authorization oAuth2Authorization = oAuth2AuthorizationService.findByToken(tokenValue, OAuth2TokenType.ACCESS_TOKEN);
            final UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                    (UsernamePasswordAuthenticationToken) Objects.requireNonNull(oAuth2Authorization).getAttributes().get(Principal.class.getName());
            return usernamePasswordAuthenticationToken.getPrincipal();
        }
        return principal;
    }
    
    @PutMapping("/oauth2/change_password")
    @Operation(summary = "修改密码")
    public void changePassword(@Validated @RequestBody ChangePasswordReq dto) {
        if (!StringUtils.equals(dto.getPassword(), dto.getConfirmPassword())) {
            throw CheckedException.badRequest("新密码与确认密码不一致");
        }
        final Long userId = authenticationContext.userId();
        this.userService.changePassword(userId, dto.getOriginalPassword(), dto.getPassword());
    }
    
    @PutMapping("/oauth2/change_info")
    @Operation(summary = "修改密码")
    public void changeInfo(@Validated @RequestBody ChangeUserInfoReq req) {
        this.userService.changeInfo(req);
    }
    
    @DeleteMapping("/oauth2/logout")
    @Operation(summary = "退出登录")
    public void logout(Principal principal) {
        if (principal instanceof JwtAuthenticationToken token) {
            final String tokenValue = token.getToken().getTokenValue();
            oAuth2AuthorizationService.remove(oAuth2AuthorizationService.findByToken(tokenValue, OAuth2TokenType.ACCESS_TOKEN));
        }
    }
    
}
