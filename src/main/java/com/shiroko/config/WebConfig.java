package com.shiroko.config;

import com.shiroko.interceptor.JwtInterceptor;
import com.shiroko.interceptor.SignInterceptor;
import com.shiroko.interceptor.UserInterceptor;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final JwtInterceptor jwtInterceptor;
    private final UserInterceptor userInterceptor;
    private final SignInterceptor signInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 用户拦截器
        registry.addInterceptor(userInterceptor)
                .addPathPatterns("/**");

        // jwt拦截器
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
                        "/v3/api-docs/**",             //  OpenAPI 文档
                        "/test/**"                      //  测试接口
                );

        // 注册签名拦截器
        registry.addInterceptor(signInterceptor)
                .addPathPatterns("/**") // 拦截所有 API
                .excludePathPatterns(
                        "/auth/**",  //  排除登录等接口
                        "/error",    //  SpringBoot 默认错误页
                        "/static/**",               //  静态资源
                        "/v3/api-docs/**",             //  OpenAPI 文档
                        "/v3/api-docs.yaml",             //  OpenAPI 文档
                        "/swagger-ui.html",           //  文档页面（如果用了 Swagger）
                        "/swagger-ui/**",           //  文档页面（如果用了 Swagger）
                        "/v3/api-docs/**",             //  OpenAPI 文档
                        "/test/**"                      //  测试接口
                );

    }
}