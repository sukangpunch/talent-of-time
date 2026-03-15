package com.example.talentoftime.fcm.scheduler;

import com.example.talentoftime.crew.domain.CrewType;
import com.example.talentoftime.fcm.domain.FcmToken;
import com.example.talentoftime.fcm.dto.FcmDataRequest;
import com.example.talentoftime.fcm.infrastructure.FcmService;
import com.example.talentoftime.fcm.repository.FcmTokenRepository;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ClassSessionNotificationScheduler {

    private final FcmTokenRepository fcmTokenRepository;
    private final FcmService firebaseCloudMessageService;

    // 1교시: 08:30 ~ 10:10
    @Scheduled(cron = "0 0 8 * * *")
    public void notifyPeriod1Entry() {
        sendToOnDutyCrews(LocalTime.of(8, 0), "수업 알림", "1교시 시작 10분 전입니다.");
    }

    @Scheduled(cron = "0 0 10 * * *")
    public void notifyPeriod1Exit() {
        sendToOnDutyCrews(LocalTime.of(10, 0), "수업 알림", "1교시 퇴실 시작 10분 전입니다.");
    }

    // 2교시: 10:30 ~ 12:10
    @Scheduled(cron = "0 0 10 * * *")
    public void notifyPeriod2Entry() {
        sendToOnDutyCrews(LocalTime.of(10, 0), "수업 알림", "2교시 시작 10분 전입니다.");
    }

    @Scheduled(cron = "0 0 12 * * *")
    public void notifyPeriod2Exit() {
        sendToOnDutyCrews(LocalTime.of(12, 0), "수업 알림", "2교시 퇴실 시작 10분 전입니다.");
    }

    // 3교시: 13:20 ~ 15:00
    @Scheduled(cron = "0 50 12 * * *")
    public void notifyPeriod3Entry() {
        sendToOnDutyCrews(LocalTime.of(12, 50), "수업 알림", "3교시 시작 10분 전입니다.");
    }

    @Scheduled(cron = "0 50 14 * * *")
    public void notifyPeriod3Exit() {
        sendToOnDutyCrews(LocalTime.of(14, 50), "수업 알림", "3교시 퇴실 시작 10분 전입니다.");
    }

    // 4교시: 15:20 ~ 17:00
    @Scheduled(cron = "0 50 14 * * *")
    public void notifyPeriod4Entry() {
        sendToOnDutyCrews(LocalTime.of(14, 50), "수업 알림", "4교시 시작 10분 전입니다.");
    }

    @Scheduled(cron = "0 50 16 * * *")
    public void notifyPeriod4Exit() {
        sendToOnDutyCrews(LocalTime.of(16, 50), "수업 알림", "4교시 퇴실 시작 10분 전입니다.");
    }

    // 5교시: 18:20 ~ 20:00
    @Scheduled(cron = "0 50 17 * * *")
    public void notifyPeriod5Entry() {
        sendToOnDutyCrews(LocalTime.of(17, 50), "수업 알림", "5교시 시작 10분 전입니다.");
    }

    @Scheduled(cron = "0 50 19 * * *")
    public void notifyPeriod5Exit() {
        sendToOnDutyCrews(LocalTime.of(19, 50), "수업 알림", "5교시 퇴실 시작 10분 전입니다.");
    }

    // 6교시: 20:20 ~ 22:00
    @Scheduled(cron = "0 50 19 * * *")
    public void notifyPeriod6Entry() {
        sendToOnDutyCrews(LocalTime.of(19, 50), "수업 알림", "6교시 시작 10분 전입니다.");
    }

    @Scheduled(cron = "0 50 21 * * *")
    public void notifyPeriod6Exit() {
        sendToOnDutyCrews(LocalTime.of(21, 50), "수업 알림", "6교시 퇴실 시작 10분 전입니다.");
    }

    private void sendToOnDutyCrews(
            LocalTime notificationTime,
            String title,
            String body
    ) {
        List<CrewType> onDutyTypes = Arrays.stream(CrewType.values())
                .filter(type -> type.isOnDutyAt(notificationTime))
                .toList();

        if (onDutyTypes.isEmpty()) {
            log.info("해당 시간({})에 근무 중인 크루 유형이 없습니다.", notificationTime);
            return;
        }

        List<FcmToken> tokens = fcmTokenRepository.findAllByCrewCrewTypeIn(onDutyTypes);
        if (tokens.isEmpty()) {
            log.info("FCM 토큰을 가진 크루가 없어 알림을 건너뜁니다.");
            return;
        }

        for (FcmToken fcmToken : tokens) {
            try {
                firebaseCloudMessageService.sendMessage(
                        new FcmDataRequest(fcmToken.getToken(), title, body)
                );
            } catch (Exception e) {
                log.error("FCM 전송 실패: tokenId={}, error={}", fcmToken.getId(), e.getMessage());
            }
        }
        log.info("알림 전송 완료: body={}, 대상 토큰 {}개 (근무 유형: {})", body, tokens.size(), onDutyTypes);
    }
}
