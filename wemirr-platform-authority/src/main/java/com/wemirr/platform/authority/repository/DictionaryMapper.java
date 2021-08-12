package com.wemirr.platform.authority.repository;

import com.wemirr.framework.boot.SuperMapper;
import com.wemirr.framework.database.configuration.dynamic.ann.DynamicDS;
import com.wemirr.platform.authority.domain.entity.common.Dictionary;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * Mapper 接口
 * 字典类型
 * </p>
 *
 * @author Levin
 * @date 2019-07-02
 */
@DynamicDS
@Repository
public interface DictionaryMapper extends SuperMapper<Dictionary> {

}
