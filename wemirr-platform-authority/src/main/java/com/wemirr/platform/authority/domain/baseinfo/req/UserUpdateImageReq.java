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
package com.wemirr.platform.authority.domain.baseinfo.req;

import com.wemirr.framework.commons.entity.SuperEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * <p>
 * 实体类
 * 用户
 * </p>
 *
 * @author Levin
 * @since 2019-11-04
 */
@Data
@Schema(name = "UserUpdateImageReq", description = "用户")
public class UserUpdateImageReq {
    
    @Schema(description = "主键")
    @NotNull(message = "id不能为空", groups = SuperEntity.Update.class)
    private Long id;
    
    /**
     * 头像
     */
    @Schema(description = "头像")
    @Length(max = 255, message = "头像长度不能超过 {max}")
    private String image;
    
}
