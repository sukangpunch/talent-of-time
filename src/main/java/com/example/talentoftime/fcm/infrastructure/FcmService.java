package com.example.talentoftime.fcm.infrastructure;

import com.example.talentoftime.common.exception.BusinessException;
import com.example.talentoftime.common.exception.ErrorCode;
import com.example.talentoftime.fcm.dto.FcmDataRequest;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FcmService {

    private final FirebaseMessaging firebaseMessaging;

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
