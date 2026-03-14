package com.example.talentoftime.fcm.dto;

import jakarta.validation.constraints.NotBlank;

public record FcmDataRequest(
        @NotBlank
        String targetToken,
        @NotBlank
        String title,
        @NotBlank
        String body
) {
}
