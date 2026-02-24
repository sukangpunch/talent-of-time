package com.example.talentoftime.common.exception;

import lombok.Builder;

@Builder
public record ErrorResponse<T>(String error, T message) {
}

