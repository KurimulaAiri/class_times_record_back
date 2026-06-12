package com.shiroko.service;

import com.shiroko.repository.entity.common.RoleBaseEntity;

/**
 * 身份查询服务 - 直接操作角色表，替代 Feign 跨服务调用
 */
public interface IdentityService {

    /**
     * 根据角色名和用户ID查询身份记录
     */
    RoleBaseEntity getByUserId(String roleName, Long userId);

    /**
     * 检查身份是否可用（存在且 is_available=true）
     */
    boolean checkAvailable(String roleName, Long userId);

    /**
     * 创建身份记录（注册时使用）
     */
    void createIdentity(String roleName, Long userId);
}
