package com.example.talentoftime.security.exception;

import com.example.talentoftime.common.exception.ErrorCode;
import com.example.talentoftime.common.exception.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
    ) throws IOException, ServletException {
        log.info("AuthenticationEntryPoint 실행");
        ErrorCode errorCode = resolveErrorCode(authException);
        String result = objectMapper.writeValueAsString(makeErrorResponse(errorCode));
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setStatus(errorCode.getHttpStatus().value());
        response.getWriter().write(result);
    }

    private ErrorCode resolveErrorCode(AuthenticationException authException) {
        if (authException instanceof CustomAuthenticationException customException) {
            return customException.getErrorCode();
        }
        return ErrorCode.AUTH_UNAUTHORIZED;
    }

    private ErrorResponse<String> makeErrorResponse(ErrorCode errorCode) {
        return ErrorResponse.<String>builder()
                .error(errorCode.getCode())
                .message(errorCode.getMessage())
                .build();
    }
}
