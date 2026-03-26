package com.example.talentoftime.fcm.infrastructure;

import com.example.talentoftime.common.exception.BusinessException;
import com.example.talentoftime.common.exception.ErrorCode;
import com.example.talentoftime.fcm.dto.FcmBroadcastRequest;
import com.example.talentoftime.fcm.dto.FcmDataRequest;
import com.example.talentoftime.fcm.repository.FcmTokenRepository;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FcmService {

    private final FirebaseMessaging firebaseMessaging;
    private final FcmTokenRepository fcmTokenRepository;

    public void sendToAll(FcmBroadcastRequest request) {
        List<String> tokens = fcmTokenRepository.findAll().stream()
                .map(fcmToken -> fcmToken.getToken())
                .toList();

        log.info("전체 FCM 전송 시작: 대상 토큰 수={}", tokens.size());
        for (String token : tokens) {
            sendMessage(new FcmDataRequest(token, request.title(), request.body()));
        }
        log.info("전체 FCM 전송 완료");
    }

    public void sendMessage(FcmDataRequest request) {
        Message message = Message.builder()
                .putData("title", request.title())
                .putData("body", request.body())
                .setToken(request.targetToken())
                .build();

        try {
            String messageId = firebaseMessaging.send(message);
            log.info("FCM 전송 성공: messageId={}, token={}", messageId, request.targetToken());
        } catch (FirebaseMessagingException e) {
            log.error("FCM 전송 실패: token={}, error={}", request.targetToken(), e.getMessage());
            throw new BusinessException(ErrorCode.FCM_SEND_FAILED);
        }
    }
}
