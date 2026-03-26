package com.example.talentoftime.crew.service;

import com.example.talentoftime.common.exception.BusinessException;
import com.example.talentoftime.common.exception.ErrorCode;
import com.example.talentoftime.crew.domain.Crew;
import com.example.talentoftime.crew.dto.CrewHeaderResponse;
import com.example.talentoftime.crew.dto.CrewResponse;
import com.example.talentoftime.crew.dto.FcmTokenRequest;
import com.example.talentoftime.crew.dto.FcmTokenResponse;
import com.example.talentoftime.crew.dto.OnboardingRequest;
import com.example.talentoftime.crew.repository.CrewRepository;
import com.example.talentoftime.fcm.domain.FcmToken;
import com.example.talentoftime.fcm.repository.FcmTokenRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MyService {

    private final CrewRepository crewRepository;
    private final FcmTokenRepository fcmTokenRepository;

    @Transactional
    public void saveFcmToken(Long crewId, FcmTokenRequest request) {
        Crew crew = crewRepository.findById(crewId)
                .orElseThrow(() -> new BusinessException(ErrorCode.CREW_NOT_FOUND));
        fcmTokenRepository.findByToken(request.fcmToken())
                .ifPresentOrElse(
                        existing -> existing.updateCrew(crew),
                        () -> fcmTokenRepository.save(new FcmToken(crew, request.fcmToken()))
                );
    }

    @Transactional(readOnly = true)
    public List<FcmTokenResponse> getFcmTokens(Long crewId) {
        return fcmTokenRepository.findAllByCrewId(crewId).stream()
                .map(FcmTokenResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public CrewHeaderResponse getHeader(Long crewId) {
        Crew crew = crewRepository.findById(crewId)
                .orElseThrow(() -> new BusinessException(ErrorCode.CREW_NOT_FOUND));
        return CrewHeaderResponse.from(crew);
    }

    @Transactional(readOnly = true)
    public CrewResponse getProfile(Long crewId) {
        Crew crew = crewRepository.findById(crewId)
                .orElseThrow(() -> new BusinessException(ErrorCode.CREW_NOT_FOUND));
        return CrewResponse.from(crew);
    }

    @Transactional
    public CrewResponse onboarding(Long id, OnboardingRequest request) {
        Crew crew = crewRepository.findById(id).orElseThrow(
                ()-> new BusinessException(ErrorCode.CREW_NOT_FOUND));
        crew.onboard(request.name(), request.crewType());

        return CrewResponse.from(crew);
    }
}
