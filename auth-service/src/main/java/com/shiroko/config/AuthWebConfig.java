package com.shiroko.config;

import com.shiroko.interceptor.JwtInterceptor;
import com.shiroko.interceptor.SignInterceptor;
import com.shiroko.interceptor.UserInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Auth Service MVC 配置
 * 注册 JwtInterceptor（小程序鉴权）、SignInterceptor（签名校验）、UserInterceptor（清理 UserContext）
 */
@Configuration
@RequiredArgsConstructor
public class AuthWebConfig implements WebMvcConfigurer {

    private final UserInterceptor userInterceptor;
    private final SignInterceptor signInterceptor;
    private final JwtInterceptor jwtInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // JWT 拦截器
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/auth/**",
                        "/internal/**",
                        "/error",
                        "/static/**",
                        "/v3/api-docs/**",
                        "/v3/api-docs.yaml",
                        "/swagger-ui.html",
                        "/swagger-ui/**",
                        "/test/**"
                );

        // 签名拦截器
        registry.addInterceptor(signInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/auth/**",
                        "/internal/**",
                        "/error",
                        "/static/**",
                        "/v3/api-docs/**",
                        "/v3/api-docs.yaml",
                        "/swagger-ui.html",
                        "/swagger-ui/**",
                        "/test/**"
                );

        // 用户拦截器（清理 UserContext，防止线程池污染）
        registry.addInterceptor(userInterceptor)
                .addPathPatterns("/**");
    }
}
