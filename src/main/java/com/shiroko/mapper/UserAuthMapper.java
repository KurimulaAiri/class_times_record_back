package com.shiroko.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shiroko.repository.entity.UserAuth;

/**
 * Description: 权限实体类
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/4/17 下午22:40
 */
public interface UserAuthMapper extends BaseMapper<UserAuth> {

    UserAuth selectAuthByAccountAndInstitution(String account, Long roleId, Long institutionId);
}




