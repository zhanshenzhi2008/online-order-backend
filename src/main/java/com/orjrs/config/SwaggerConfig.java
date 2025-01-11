package com.orjrs.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger配置类
 */
@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "在线点餐系统API",
                description = "在线点餐系统后端接口文档",
                version = "v1.0.0",
                license = @License(
                        name = "Apache 2.0",
                        url = "http://springdoc.org"
                )
        )
)
public class SwaggerConfig {
} 