package com.example.talentoftime.common.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("시대인재 업무 스케줄 API")
                        .description("크루(알바생)의 일일 업무 스케줄을 자동 배정하고 관리하는 API")
                        .version("v1.0"));
    }
}
