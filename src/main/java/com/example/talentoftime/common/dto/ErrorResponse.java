package com.example.talentoftime.common.dto;

import com.example.talentoftime.common.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrorResponse {

    private String code;
    private String message;

    public static ErrorResponse from(ErrorCode errorCode) {
        ErrorResponse response = new ErrorResponse();
        response.code = errorCode.getCode();
        response.message = errorCode.getMessage();
        return response;
    }
}
