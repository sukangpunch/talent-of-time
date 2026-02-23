package com.example.talentoftime.crew.service;

import com.example.talentoftime.common.exception.BusinessException;
import com.example.talentoftime.common.exception.ErrorCode;
import com.example.talentoftime.count.dto.MyCountResponse;
import com.example.talentoftime.count.repository.CountRepository;
import com.example.talentoftime.crew.domain.Crew;
import com.example.talentoftime.crew.dto.CrewResponse;
import com.example.talentoftime.crew.repository.CrewRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MyService {

    private final CrewRepository crewRepository;
    private final CountRepository countRepository;

    @Transactional(readOnly = true)
    public CrewResponse getProfile(Long crewId) {
        Crew crew = crewRepository.findById(crewId)
                .orElseThrow(() -> new BusinessException(ErrorCode.CREW_NOT_FOUND));
        return CrewResponse.from(crew);
    }

    @Transactional(readOnly = true)
    public List<MyCountResponse> getMyCounts(Long crewId) {
        Crew crew = crewRepository.findById(crewId)
                .orElseThrow(() -> new BusinessException(ErrorCode.CREW_NOT_FOUND));
        return countRepository.findByCrew(crew).stream()
                .map(MyCountResponse::from)
                .toList();
    }
}
