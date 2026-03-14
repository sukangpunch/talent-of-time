package com.example.talentoftime.fcm.controller;

import com.example.talentoftime.fcm.dto.FcmDataRequest;
import com.example.talentoftime.fcm.infrastructure.FcmService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "FCM", description = "FCM 푸시 알림 API")
@RestController
@RequestMapping("/api/v1/fcm")
@RequiredArgsConstructor
public class FcmController {

    private final FcmService fcmService;

    @Operation(
            summary = "FCM 푸시 알림 전송",
            description = "특정 디바이스 토큰으로 푸시 알림을 전송합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "전송 성공"),
            @ApiResponse(responseCode = "500", description = "FCM 전송 실패")
    })
    @PostMapping("/send")
    public ResponseEntity<Void> sendMessage(@Valid @RequestBody FcmDataRequest request) {
        fcmService.sendMessage(request);
        return ResponseEntity.ok().build();
    }
}
