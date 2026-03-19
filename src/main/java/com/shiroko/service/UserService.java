package com.shiroko.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shiroko.repository.entity.User;

/**
 * Description: TODO
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/3/19 下午11:34
 */
public interface UserService extends IService<User> {
    Long saveOrUpdateUser(String openid);
}
