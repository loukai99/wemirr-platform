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

package com.wemirr.framework.security.domain;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.google.common.collect.Lists;
import com.wemirr.framework.commons.security.DataPermission;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;

import java.time.LocalDate;
import java.util.*;

/**
 * @author Levin
 * @since 2019-04-03
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoDetails implements UserDetails, OAuth2AuthenticatedPrincipal, CredentialsContainer {
    
    private Long userId;
    private String email;
    private Integer sex;
    private String mobile;
    private Long tenantId;
    private String tenantCode;
    private String tenantName;
    private String nickName;
    private String realName;
    private String username;
    private String password;
    private String avatar;
    private Boolean enabled;
    private String description;
    private LocalDate birthday;
    private Long orgId;
    /**
     * 功能权限（资源码）
     */
    @Builder.Default
    private Collection<String> funcPermissions = new ArrayList<>();
    @Builder.Default
    private Collection<String> roles = new ArrayList<>();
    
    private Collection<GrantedAuthority> authorities;
    @Builder.Default
    private Map<String, Object> attributes = new HashMap<>();
    
    /**
     * 数据权限(可视范围)
     */
    private DataPermission dataPermission;
    
    @Override
    public void eraseCredentials() {
        this.password = null;
    }
    
    @Override
    public Map<String, Object> getAttributes() {
        return ObjectUtil.defaultIfNull(attributes, Map.of());
    }
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (CollUtil.isNotEmpty(authorities)) {
            return this.authorities;
        }
        final List<GrantedAuthority> authorities = Lists.newArrayList();
        if (CollUtil.isNotEmpty(roles)) {
            authorities.addAll(roles.stream().map(x -> new CustomGrantedAuthority(x, true)).toList());
        }
        if (CollUtil.isNotEmpty(funcPermissions)) {
            authorities.addAll(funcPermissions.stream().map(CustomGrantedAuthority::new).toList());
        }
        return authorities;
    }
    
    @Override
    public String getPassword() {
        return this.password;
    }
    
    @Override
    public String getUsername() {
        return this.username;
    }
    
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    
    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
    
    @Override
    public String getName() {
        return this.username;
    }
}
