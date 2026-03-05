package com.example.talentoftime.auth.dto;

public record LoginResponse(
        String accessToken,
        boolean isOnboarded
) {
}
