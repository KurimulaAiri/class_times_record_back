package com.shiroko.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shiroko.repository.entity.Permission;
import com.shiroko.repository.entity.RoleBaseEntity;
import com.shiroko.repository.entity.User;
import com.shiroko.repository.vo.UserVO;

/**
 * Description: TODO
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/3/19 下午11:34
 */
public interface UserService extends IService<User> {
    Long saveOrUpdateUser(String openid);

    UserVO<RoleBaseEntity> getFullUserInfoByOpenid(String openid, Permission permission);
}
