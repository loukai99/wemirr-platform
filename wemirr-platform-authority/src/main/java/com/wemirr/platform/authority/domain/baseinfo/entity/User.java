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

package com.wemirr.platform.authority.domain.baseinfo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wemirr.framework.commons.entity.SuperEntity;
import com.wemirr.platform.authority.domain.baseinfo.enums.Sex;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

import static com.baomidou.mybatisplus.annotation.SqlCondition.LIKE;

/**
 * <p>
 * 实体类
 * 用户
 * </p>
 *
 * @author Levin
 * @since 2020-02-14
 */
@Data
@SuperBuilder
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_user")
@Schema(name = "User", description = "用户")
public class User extends SuperEntity<Long> {
    
    /**
     * 账号
     */
    @TableField(value = "username")
    @Schema(description = "账号")
    private String username;
    
    @Schema(description = "租户ID")
    private Long tenantId;
    /**
     * 密码
     */
    @TableField(value = "password")
    @Schema(description = "密码")
    private String password;
    /**
     * 昵称
     */
    @TableField(value = "nick_name", condition = LIKE)
    @Schema(description = "昵称")
    private String nickName;
    
    @Schema(description = "描述")
    private String description;
    
    @Schema(description = "身份证")
    private String idCard;
    
    /**
     * 邮箱
     */
    @TableField(value = "email", condition = LIKE)
    @Schema(description = "邮箱")
    private String email;
    
    /**
     * 手机
     */
    @TableField(value = "mobile", condition = LIKE)
    @Schema(description = "手机号")
    private String mobile;
    
    /**
     * 性别
     */
    @TableField(value = "sex")
    @Schema(description = "性别")
    private Sex sex;
    
    /**
     * 头像
     */
    @TableField(value = "avatar")
    @Schema(description = "头像")
    private String avatar;
    
    @Schema(description = "是否只读")
    private Boolean readonly;
    
    @Schema(description = "状态")
    private Boolean status;
    
    @Schema(description = "民族")
    private String nation;
    
    @Schema(description = "学历")
    private String education;
    
    @Schema(description = "生日")
    private LocalDate birthday;
    
    @Schema(description = "机构ID")
    private Long orgId;
    
    @Schema(description = "岗位ID")
    private Long stationId;
    
    @Schema(description = "职位状态")
    private String positionStatus;
    
}
