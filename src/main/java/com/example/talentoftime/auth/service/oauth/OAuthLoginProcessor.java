package com.example.talentoftime.auth.service.oauth;

import com.example.talentoftime.auth.domain.AccessToken;
import com.example.talentoftime.auth.dto.LoginResponse;
import com.example.talentoftime.auth.dto.oauth.OAuthUserInfo;
import com.example.talentoftime.auth.service.AuthTokenProvider;
import com.example.talentoftime.common.domain.TaskType;
import com.example.talentoftime.count.domain.Count;
import com.example.talentoftime.count.repository.CountRepository;
import com.example.talentoftime.crew.domain.Crew;
import com.example.talentoftime.crew.domain.Role;
import com.example.talentoftime.crew.repository.CrewRepository;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class OAuthLoginProcessor {

    private final CrewRepository crewRepository;
    private final CountRepository countRepository;
    private final AuthTokenProvider authTokenProvider;

    @Transactional
    public LoginResponse process(OAuthUserInfo oAuthUserInfo) {
        Crew crew = findOrCreateCrew(oAuthUserInfo);
        AccessToken accessToken = authTokenProvider.generateAccessToken(crew);
        return LoginResponse.of(accessToken, crew.isOnboarded());
    }

    private Crew findOrCreateCrew(OAuthUserInfo oAuthUserInfo) {
        String providerId = oAuthUserInfo.getProvider() + "_" + oAuthUserInfo.getProviderId();
        return crewRepository.findByProviderId(providerId)
                .orElseGet(() -> createCrew(oAuthUserInfo, providerId));
    }

    private Crew createCrew(OAuthUserInfo oAuthUserInfo, String providerId) {
        log.info("[OAuthLoginProcessor] 신규 크루 가입. email: {}, providerId: {}", oAuthUserInfo.getEmail(), providerId);
        Crew crew = new Crew(
                oAuthUserInfo.getName(),
                oAuthUserInfo.getEmail(),
                providerId,
                Role.USER
        );
        crewRepository.save(crew);
        initializeCounts(crew);
        log.info("[OAuthLoginProcessor] 신규 크루 가입 완료. crewId: {}", crew.getId());
        return crew;
    }

    private void initializeCounts(Crew crew) {
        List<Count> counts = Arrays.stream(TaskType.values())
                .map(taskType -> Count.createInitial(crew, taskType))
                .toList();
        countRepository.saveAll(counts);
    }
}
