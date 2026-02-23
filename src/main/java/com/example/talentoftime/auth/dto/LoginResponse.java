package com.example.talentoftime.auth.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginResponse {

    private String accessToken;
    private String tokenType = "Bearer";
    private Long crewId;
    private String name;
    private String role;

    public static LoginResponse of(String accessToken, Long crewId, String name, String role) {
        LoginResponse response = new LoginResponse();
        response.accessToken = accessToken;
        response.crewId = crewId;
        response.name = name;
        response.role = role;
        return response;
    }
}
