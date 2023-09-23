package com.wemirr.platform.authority.repository.common;

import com.wemirr.framework.db.mybatisplus.ext.SuperMapper;
import com.wemirr.platform.authority.domain.common.entity.SiteNotify;
import org.springframework.stereotype.Repository;

/**
 * @author Levin
 */
@Repository
public interface SiteMessagePublishMapper extends SuperMapper<SiteNotify> {
}