package com.wemirr.platform.authority.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wemirr.framework.db.mybatisplus.ext.SuperServiceImpl;
import com.wemirr.framework.db.mybatisplus.intercept.data.DataPermission;
import com.wemirr.framework.db.mybatisplus.intercept.data.DataScopeType;
import com.wemirr.framework.db.mybatisplus.wrap.Wraps;
import com.wemirr.framework.db.mybatisplus.wrap.query.LbqWrapper;
import com.wemirr.platform.authority.domain.entity.baseinfo.Station;
import com.wemirr.platform.authority.domain.req.StationPageReq;
import com.wemirr.platform.authority.repository.StationMapper;
import com.wemirr.platform.authority.service.StationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


/**
 * <p>
 * 业务实现类
 * 岗位
 * </p>
 *
 * @author Levin
 * @since 2019-07-22
 */
@Slf4j
@Service
public class StationServiceImpl extends SuperServiceImpl<StationMapper, Station> implements StationService {


    @Override
    public IPage<Station> pageList(StationPageReq req) {
        final LbqWrapper<Station> wrapper = Wraps.<Station>lbQ().like(Station::getName, req.getName())
                .eq(Station::getType, req.getType())
                .eq(Station::getOrgId, req.getOrgId())
                .eq(Station::getStatus, req.getStatus())
                .orderByAsc(Station::getSequence);
        return baseMapper.findStationPage(req.buildPage(), wrapper, DataPermission.builder().scopeType(DataScopeType.ALL).build());
    }
}
