package com.shiroko.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shiroko.repository.entity.Permission;
import com.shiroko.repository.entity.User;
import com.shiroko.repository.entity.UserPlatform;
import com.shiroko.repository.entity.common.RoleBaseEntity;
import com.shiroko.repository.vo.UserVO;

/**
 * Description: 用户服务接口
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/3/19 下午11:34
 */
public interface UserService extends IService<User> {
    UserPlatform saveOrUpdateUser(String openid, String platform);

    UserVO<RoleBaseEntity> getFullUserInfoByPlatformOpenid(String platform, String openid, Permission permission);

    UserVO<RoleBaseEntity> getFullUserInfoByUserId(Long userId, Permission permission);

    UserVO<RoleBaseEntity> getFullUserInfoByPlatformOpenidAndInstitution(String platform, String openId, Long institutionId, Permission permission);
}
