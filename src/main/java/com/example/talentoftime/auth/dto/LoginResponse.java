package com.example.talentoftime.auth.dto;

import com.example.talentoftime.auth.domain.AccessToken;
import jakarta.validation.constraints.NotNull;

public record LoginResponse(

        @NotNull
        String accessToken,
        boolean isOnboarded
) {
    public static LoginResponse of(
            AccessToken accessToken,
            boolean isOnboarded
    ) {
        return new LoginResponse(accessToken.token(), isOnboarded);
    }
}
