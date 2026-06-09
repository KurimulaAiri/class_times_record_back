package com.shiroko.filter;

import com.shiroko.support.RepeatedlyRequestWrapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Description: 请求缓存过滤器
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/5/7 下午5:32
 */
@Component
public class RequestCachingFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        // 建议增加判断：只对 JSON 请求进行包装，防止影响文件上传
        String contentType = request.getContentType();
        if (contentType != null && contentType.contains("application/json")) {
            // 包装请求
            RepeatedlyRequestWrapper wrappedRequest = new RepeatedlyRequestWrapper(request);
            filterChain.doFilter(wrappedRequest, response);
        } else {
            filterChain.doFilter(request, response);
        }
    }
}