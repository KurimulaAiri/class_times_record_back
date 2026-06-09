package com.shiroko.config;

import com.shiroko.interceptor.SignInterceptor;
import com.shiroko.interceptor.UserInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Description: Spring MVC 配置类
 * 微服务架构下 JWT 校验由网关统一处理，下游服务不再注册 JwtInterceptor
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/3/19 下午11:15
 */
@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final UserInterceptor userInterceptor;
    private final SignInterceptor signInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 用户拦截器（清理 UserContext，防止线程池污染）
        registry.addInterceptor(userInterceptor)
                .addPathPatterns("/**");

        // 注册签名拦截器
        registry.addInterceptor(signInterceptor)
                .addPathPatterns("/**") // 拦截所有API
                .excludePathPatterns(
                        "/auth/**",  //  排除登录等接口
                        "/error",    //  SpringBoot 默认错误页
                        "/static/**",               //  静态资源
                        "/v3/api-docs/**",             //  OpenAPI 文档
                        "/v3/api-docs.yaml",             //  OpenAPI 文档
                        "/swagger-ui.html",           //  文档页面（如果用Swagger）
                        "/swagger-ui/**",           //  文档页面（如果用Swagger）
                        "/test/**"                      //  测试接口
                );

    }
}
