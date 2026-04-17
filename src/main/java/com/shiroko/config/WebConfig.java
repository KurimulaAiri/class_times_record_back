package com.shiroko.config;

import com.shiroko.interceptor.JwtInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Description: Spring MVC 配置类
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/3/19 下午11:15
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final JwtInterceptor jwtInterceptor;

    // 注入你刚才写的 JWT 拦截器
    public WebConfig(JwtInterceptor jwtInterceptor) {
        this.jwtInterceptor = jwtInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/**")             // 默认拦截所有路径
                .excludePathPatterns(               // 以下路径不拦截：
                        "/auth/login_no_pwd",         //  登录接口（无需密码）
                        "/auth/login_by_pwd",         //  登录接口（需要密码）
                        "/auth/get_open_id",          //  获取openid接口
                        "/auth/register",           //  注册接口（如果有）
                        "/error",                   //  SpringBoot 默认错误页
                        "/static/**",               //  静态资源
                        "/v3/api-docs/**",             //  OpenAPI 文档
                        "/v3/api-docs.yaml",             //  OpenAPI 文档
                        "/swagger-ui.html",           //  文档页面（如果用了 Swagger）
                        "/swagger-ui/**",           //  文档页面（如果用了 Swagger）
                        "/v3/api-docs/**"
                );
    }
}