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
package com.wemirr.platform.authority.domain.baseinfo.resp;

import com.wemirr.platform.authority.domain.baseinfo.enums.ResourceType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 构建 Vue路由
 *
 * @author Levin
 * @since 2019-10-20 15:17
 */
@Data
public class VueRouter {
    
    private Long id;
    private Long parentId;
    @Schema(description = "路径")
    private String path;
    @Schema(description = "按钮名称")
    private String name;
    @Schema(description = "菜单名称")
    private String label;
    @Schema(description = "图标")
    private String icon;
    @Schema(description = "组件")
    private String component;
    @Schema(description = "重定向")
    private String redirect;
    
    private String model;
    
    private String permission;
    /**
     * 排序
     */
    @Schema(description = "排序")
    private Integer sequence;
    @Schema(description = "类型（1=菜单;2=按钮;3=路由;5=一键发布模板）")
    private ResourceType type;
    
    private Boolean global;
    private Boolean status;
    @Schema(description = "显示/隐藏")
    private Boolean display;
    private String description;
}
