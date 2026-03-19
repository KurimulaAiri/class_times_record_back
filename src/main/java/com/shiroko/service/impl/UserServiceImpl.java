package com.shiroko.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shiroko.mapper.UserMapper;
import com.shiroko.repository.entity.User;
import com.shiroko.service.UserService;
import org.springframework.stereotype.Service;

/**
 * Description: TODO
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/3/19 上午23:34
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public Long saveOrUpdateUser(String openid) {
        User user = this.getOne(new LambdaQueryWrapper<User>().eq(User::getOpenid, openid));
        if (user == null) {
            user = new User();
            user.setOpenid(openid);
        }
        this.saveOrUpdate(user);
        return user.getId();
    }
}




