package com.example.talentoftime.fcm.dto;

import jakarta.validation.constraints.NotBlank;

public record FcmBroadcastRequest(
        @NotBlank
        String title,
        @NotBlank
        String body
) {
}
