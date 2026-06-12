package com.shiroko.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouteConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("cr-auth-service", r -> r
                        .path("/auth/**")
                        .filters(f -> f.stripPrefix(1))
                        .uri("lb://cr-auth-service"))
                .route("cr-business-service", r -> r
                        .path("/biz/**")
                        .filters(f -> f.stripPrefix(1))
                        .uri("lb://cr-business-service"))
                .route("cr-admin-service", r -> r
                        .path("/admin/**")
                        .filters(f -> f.stripPrefix(1))
                        .uri("lb://cr-admin-service"))
                .build();
    }
}
