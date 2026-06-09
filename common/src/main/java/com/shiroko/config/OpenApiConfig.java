package com.shiroko.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Description: OpenAPI 配置类
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/5/6 上午12:14
 */
@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .addServersItem(new Server().url("https://api.kurimula-airi.top").description("生产环境"))
                .addServersItem(new Server().url("http://localhost:9999").description("测试环境"));
    }
}
