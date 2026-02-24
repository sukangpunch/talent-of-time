package com.example.talentoftime.auth.token.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "token")
public record TokenProperties(
        TokenConfig access
) {
}
