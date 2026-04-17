package com.shiroko.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shiroko.repository.entity.UserAuth;
import com.shiroko.service.UserAuthService;
import com.shiroko.mapper.UserAuthMapper;
import org.springframework.stereotype.Service;

/**
 * Description: 权限实体类实现类
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/4/17 下午22:40
 */
@Service
public class UserAuthServiceImpl extends ServiceImpl<UserAuthMapper, UserAuth>
    implements UserAuthService{

}




