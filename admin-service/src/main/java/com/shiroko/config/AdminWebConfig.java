package com.shiroko.config;

import com.shiroko.interceptor.AdminJwtInterceptor;
import com.shiroko.interceptor.SignInterceptor;
import com.shiroko.interceptor.UserInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Admin Service MVC 配置
 * 注册 AdminJwtInterceptor（管理后台鉴权）、SignInterceptor（签名校验）、UserInterceptor（清理 UserContext）
 */
@Configuration
@RequiredArgsConstructor
public class AdminWebConfig implements WebMvcConfigurer {

    private final AdminJwtInterceptor adminJwtInterceptor;
    private final SignInterceptor signInterceptor;
    private final UserInterceptor userInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // Admin JWT 拦截器
        registry.addInterceptor(adminJwtInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/user/login",
                        "/user/refresh",
                        "/crypto/public_key",
                        "/error",
                        "/static/**",
                        "/v3/api-docs/**",
                        "/v3/api-docs.yaml",
                        "/swagger-ui.html",
                        "/swagger-ui/**"
                );

        // 签名拦截器
        registry.addInterceptor(signInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/crypto/public_key",
                        "/error",
                        "/static/**",
                        "/v3/api-docs/**",
                        "/v3/api-docs.yaml",
                        "/swagger-ui.html",
                        "/swagger-ui/**"
                );

        // 用户拦截器（清理 UserContext，防止线程池污染）
        registry.addInterceptor(userInterceptor)
                .addPathPatterns("/**");
    }
}
