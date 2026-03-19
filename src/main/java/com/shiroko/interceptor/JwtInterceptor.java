package com.shiroko.interceptor;

import com.shiroko.context.UserContext;
import com.shiroko.util.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Description: TODO
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/3/19 下午5:07
 */
@Component
public class JwtInterceptor implements HandlerInterceptor {

    private final JwtUtils jwtUtils;

    @Autowired
    private JwtInterceptor(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull Object handler) {
        // 从 Header 中获取 token（对应你 request.js 里的 'token' 字段）
        String token = request.getHeader("token");

        if (token != null) {
            Long userId = jwtUtils.getUserIdFromToken(token);
            if (userId != null) {
                // 存入当前线程，方便后续 Service 使用
                UserContext.setUserId(userId);
                return true;
            }
        }

        // 校验失败返回 401 错误码
        response.setStatus(401);
        return false;
    }

    @Override
    public void afterCompletion(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull Object handler,
            @NonNull Exception ex) {
        // 请求结束，务必清理 ThreadLocal 防止内存泄漏
        UserContext.remove();
    }
}
