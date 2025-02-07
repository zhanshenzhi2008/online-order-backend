package com.orjrs.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("在线点餐系统接口文档")
                .description("提供商品管理、订单管理等接口")
                .version("1.0.0")
                .contact(new Contact()
                    .name("开发团队")
                    .email("dev@example.com")
                    .url("http://localhost:8080"))
                .license(new License()
                    .name("MIT")
                    .url("https://opensource.org/licenses/MIT"))
            );
    }
} 