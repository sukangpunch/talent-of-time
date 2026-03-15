package com.example.talentoftime.crew.dto;

import com.example.talentoftime.fcm.domain.FcmToken;

public record FcmTokenResponse(
        String token
) {

    public static FcmTokenResponse from(FcmToken fcmToken) {
        return new FcmTokenResponse(fcmToken.getToken());
    }
}
