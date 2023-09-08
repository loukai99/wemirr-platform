package com.wemirr.framework.security.server.integration.custom;

import cn.hutool.core.util.StrUtil;
import com.wemirr.framework.security.constant.SecurityConstants;
import com.wemirr.framework.security.exception.OAuth2InvalidException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Optional;

/**
 * 短信验证码校验实现
 *
 * @author Levin
 */
@Slf4j
public class CustomLoginAuthenticationProvider extends DaoAuthenticationProvider {

    /**
     * 利用构造方法在通过{@link Component}注解初始化时
     * 注入UserDetailsService和passwordEncoder，然后
     * 设置调用父类关于这两个属性的set方法设置进去
     *
     * @param userDetailsService 用户服务，给框架提供用户信息
     * @param passwordEncoder    密码解析器，用于加密和校验密码
     */
    public CustomLoginAuthenticationProvider(UserDetailsService userDetailsService,
                                             PasswordEncoder passwordEncoder) {
        super.setPasswordEncoder(passwordEncoder);
        super.setUserDetailsService(userDetailsService);
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        log.info("authenticate custom...");
        return super.authenticate(authentication);
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        log.info("进入自定义凭证验证流程....");
        if (authentication.getCredentials() == null) {
            this.logger.error("Failed to authenticate since no credentials provided");
            throw new BadCredentialsException("The credentials cannot be empty.");
        }
        // 获取当前request
        HttpServletRequest request = Optional.ofNullable(RequestContextHolder.getRequestAttributes())
                .map(req -> ((ServletRequestAttributes) req).getRequest())
                .orElseThrow(() -> new OAuth2InvalidException("Failed to get the current request."));
        // 获取当前登录方式
        String loginType = request.getParameter(SecurityConstants.LOGIN_TYPE_NAME);
        if (StrUtil.equals(loginType, OAuth2ParameterNames.PASSWORD)) {
            // 其它调用父类默认实现的密码方式登录
            super.additionalAuthenticationChecks(userDetails, authentication);
        }
    }


}
