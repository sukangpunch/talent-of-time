package com.example.talentoftime.crew.dto;

import jakarta.validation.constraints.NotBlank;

public record FcmTokenRequest(
        @NotBlank
        String fcmToken
) {
}
