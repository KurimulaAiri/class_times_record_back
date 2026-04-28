package com.shiroko.interceptor;

import com.shiroko.context.UserContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Description: 用户拦截器
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/4/29 上午1:07
 */
@Component
public class UserInterceptor implements HandlerInterceptor {
    @Override
    public void afterCompletion(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull Object handler,
            @NonNull Exception ex
    ) {
        // 请求处理完成后，务必注销 UserContext，防止线程池污染
        UserContext.remove();
    }
}
