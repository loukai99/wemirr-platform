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

package com.wemirr.platform.authority.service.impl;

import com.wemirr.framework.commons.exception.CheckedException;
import com.wemirr.framework.db.mybatisplus.ext.SuperServiceImpl;
import com.wemirr.framework.db.mybatisplus.wrap.Wraps;
import com.wemirr.platform.authority.domain.common.entity.Dictionary;
import com.wemirr.platform.authority.domain.common.entity.DictionaryItem;
import com.wemirr.platform.authority.repository.common.DictionaryItemMapper;
import com.wemirr.platform.authority.repository.common.DictionaryMapper;
import com.wemirr.platform.authority.service.DictionaryItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * <p>
 * 业务实现类
 * 字典项
 * </p>
 *
 * @author Levin
 * @since 2019-07-02
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DictionaryItemServiceImpl extends SuperServiceImpl<DictionaryItemMapper, DictionaryItem> implements DictionaryItemService {
    
    private final DictionaryMapper dictionaryMapper;
    
    @Override
    public void addDictionaryItem(Long dictionaryId, DictionaryItem item) {
        final long count = this.baseMapper.selectCount(Wraps.<DictionaryItem>lbQ()
                .eq(DictionaryItem::getValue, item.getValue())
                .eq(DictionaryItem::getDictionaryId, dictionaryId));
        if (count > 0) {
            throw CheckedException.badRequest("编码已存在");
        }
        final Dictionary dictionary = Optional.ofNullable(this.dictionaryMapper.selectById(dictionaryId))
                .orElseThrow(() -> CheckedException.notFound("码表不存在"));
        item.setDictionaryId(dictionaryId);
        item.setDictionaryCode(dictionary.getCode());
        this.baseMapper.insert(item);
    }
    
    @Override
    public void editDictionaryItem(Long dictionaryId, DictionaryItem item) {
        final long count = this.baseMapper.selectCount(Wraps.<DictionaryItem>lbQ()
                .eq(DictionaryItem::getValue, item.getValue())
                .ne(DictionaryItem::getId, item.getId())
                .eq(DictionaryItem::getDictionaryId, dictionaryId));
        if (count > 0) {
            throw CheckedException.badRequest("编码已存在");
        }
        this.baseMapper.updateById(item);
    }
}
