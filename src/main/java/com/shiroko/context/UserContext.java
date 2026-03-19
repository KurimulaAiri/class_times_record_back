package com.shiroko.context;

/**
 * Description: TODO
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/3/19 下午5:05
 */
public class UserContext {
    private static final ThreadLocal<Long> userThreadLocal = new ThreadLocal<>();

    public static void setUserId(Long userId) {
        userThreadLocal.set(userId);
    }

    public static Long getUserId() {
        return userThreadLocal.get();
    }

    public static void remove() {
        userThreadLocal.remove();
    }
}
