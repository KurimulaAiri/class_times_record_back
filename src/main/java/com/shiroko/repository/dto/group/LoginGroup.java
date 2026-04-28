package com.shiroko.repository.dto.group;

/**
 * Description: 登录参数校验分组
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/4/23 下午2:50
 */
public interface LoginGroup {

    interface LoginNoPwd {}

    interface LoginByPwd {}

    interface LoginByToken {
    }

    interface LoginOut {
    }
}
