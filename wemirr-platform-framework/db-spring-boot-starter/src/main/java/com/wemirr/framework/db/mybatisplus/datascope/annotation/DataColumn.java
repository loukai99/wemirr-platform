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

package com.wemirr.framework.db.mybatisplus.datascope.annotation;

import com.wemirr.framework.commons.entity.Entity;
import com.wemirr.framework.commons.security.DataResourceType;
import com.wemirr.framework.commons.security.DataScopeType;

import java.lang.annotation.*;

/**
 * @author Levin
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface DataColumn {
    
    /**
     * 表别名
     *
     * @return 别名
     */
    String alias() default "";
    
    /**
     * 字段名称
     *
     * @return 字段名称
     */
    String name() default Entity.CREATE_USER_COLUMN;
    
    Class<?> javaClass() default Long.class;
    
    /**
     * 权限资源范围(默认创建人)
     *
     * @return 资源
     */
    DataResourceType resource() default DataResourceType.USER;
    
    /**
     * 权限范围（默认跟随系统，如果指定了就跟着指定走）
     */
    DataScopeType scopeType() default DataScopeType.IGNORE;
    
}
