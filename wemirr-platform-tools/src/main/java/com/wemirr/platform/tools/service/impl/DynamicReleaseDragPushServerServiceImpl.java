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
package com.wemirr.platform.tools.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.wemirr.framework.db.mybatisplus.ext.SuperServiceImpl;
import com.wemirr.framework.db.mybatisplus.wrap.Wraps;
import com.wemirr.platform.tools.domain.entity.DynamicReleaseDrag;
import com.wemirr.platform.tools.domain.entity.DynamicReleaseDragPushServer;
import com.wemirr.platform.tools.domain.entity.DynamicReleaseDragPushServerModel;
import com.wemirr.platform.tools.domain.req.BatchKey;
import com.wemirr.platform.tools.domain.resp.DynamicReleaseDragPushServerModelResp;
import com.wemirr.platform.tools.repository.DynamicReleaseDragMapper;
import com.wemirr.platform.tools.repository.DynamicReleaseDragPushServerMapper;
import com.wemirr.platform.tools.repository.DynamicReleaseDragPushServerModelMapper;
import com.wemirr.platform.tools.service.DynamicReleaseDragPushServerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Levin
 */
@Service
@RequiredArgsConstructor
public class DynamicReleaseDragPushServerServiceImpl extends SuperServiceImpl<DynamicReleaseDragPushServerMapper, DynamicReleaseDragPushServer> implements DynamicReleaseDragPushServerService {
    
    private final DynamicReleaseDragPushServerModelMapper dynamicReleaseDragPushServerModelMapper;
    private final DynamicReleaseDragMapper dynamicReleaseDragMapper;
    
    @Override
    public DynamicReleaseDragPushServerModelResp queryModelByPushId(Long pushId) {
        final DynamicReleaseDragPushServerModelResp.DynamicReleaseDragPushServerModelRespBuilder builder = DynamicReleaseDragPushServerModelResp.builder();
        final List<DynamicReleaseDrag> dynamicReleaseDrags = this.dynamicReleaseDragMapper.selectList(Wraps.lbQ());
        if (CollectionUtil.isNotEmpty(dynamicReleaseDrags)) {
            builder.modelList(dynamicReleaseDrags.stream().map(drag -> DynamicReleaseDragPushServerModelResp.DynamicReleaseDragModel.builder()
                    .id(drag.getId()).label(drag.getLabel()).build()).collect(Collectors.toList()));
        }
        final List<DynamicReleaseDragPushServerModel> list = dynamicReleaseDragPushServerModelMapper.selectList(Wraps.<DynamicReleaseDragPushServerModel>lbQ()
                .eq(DynamicReleaseDragPushServerModel::getPushId, pushId));
        if (CollectionUtil.isNotEmpty(list)) {
            builder.checkedModels(list.stream().map(DynamicReleaseDragPushServerModel::getDragId).collect(Collectors.toList()));
        }
        return builder.build();
    }
    
    @Override
    @DSTransactional
    public void binding(Long id, BatchKey<Long> ids) {
        this.dynamicReleaseDragPushServerModelMapper.deleteById(id);
        if (ids == null || CollectionUtil.isEmpty(ids.getIds())) {
            return;
        }
        for (Long dragId : ids.getIds()) {
            this.dynamicReleaseDragPushServerModelMapper.insert(DynamicReleaseDragPushServerModel.builder()
                    .pushId(id).dragId(dragId).build());
        }
    }
}
