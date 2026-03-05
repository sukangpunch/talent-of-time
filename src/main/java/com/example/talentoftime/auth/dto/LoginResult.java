package com.example.talentoftime.auth.dto;

public record LoginResult(
        String accessToken,
        String refreshToken,
        boolean isOnboarded
) {
}
