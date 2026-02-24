package com.example.talentoftime.auth.token.config;

import java.time.Duration;

public record TokenConfig(
        String storageKeyPrefix,
        Duration expireTime
) {
}
