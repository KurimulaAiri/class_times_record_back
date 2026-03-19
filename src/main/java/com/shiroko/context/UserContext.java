package com.shiroko.context;

import com.shiroko.repository.entity.User;

/**
 * Description: 用户上下文
 * 用于存储和获取当前线程的用户信息
 * @author Guguguy
 * @version 1.0
 * @since 2026/3/19 下午5:05
 */
public class UserContext {
    private static final ThreadLocal<User> userThreadLocal = new ThreadLocal<>();

    public static void setUser(User user) {
        userThreadLocal.set(user);
    }

    public static User getUser() {
        return userThreadLocal.get();
    }

    public static void remove() {
        userThreadLocal.remove();
    }
}
