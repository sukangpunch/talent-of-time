package com.example.talentoftime.auth.domain;

public record AccessToken(
        String token
) implements Token {
}

