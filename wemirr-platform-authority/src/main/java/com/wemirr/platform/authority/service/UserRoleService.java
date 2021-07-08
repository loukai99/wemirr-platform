package com.wemirr.platform.authority.service;

import com.wemirr.framework.boot.service.SuperService;
import com.wemirr.platform.authority.domain.entity.UserRole;
import com.wemirr.platform.authority.domain.vo.UserRoleResp;

/**
 * <p>
 * 业务接口
 * 角色分配
 * 账号角色绑定
 * </p>
 *
 * @author Levin
 * @since 2019-07-03
 */
public interface UserRoleService extends SuperService<UserRole> {

    UserRoleResp findUserByRoleId(Long roleId);

}
