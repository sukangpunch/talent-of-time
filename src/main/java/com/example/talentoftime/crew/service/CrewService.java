package com.example.talentoftime.crew.service;

import com.example.talentoftime.common.exception.BusinessException;
import com.example.talentoftime.common.exception.ErrorCode;
import com.example.talentoftime.crew.domain.Crew;
import com.example.talentoftime.crew.domain.CrewType;
import com.example.talentoftime.crew.dto.CrewResponse;
import com.example.talentoftime.crew.dto.CrewUpdateRequest;
import com.example.talentoftime.crew.repository.CrewRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CrewService {

    private final CrewRepository crewRepository;

    @Transactional(readOnly = true)
    public CrewResponse findCrew(Long crewId) {
        Crew crew = findCrewOrThrow(crewId);
        return CrewResponse.from(crew);
    }

    @Transactional(readOnly = true)
    public CrewResponse findCrewByName(String name) {
        Crew crew = crewRepository.findByName(name)
                .orElseThrow(() -> new BusinessException(ErrorCode.CREW_NOT_FOUND));
        return CrewResponse.from(crew);
    }

    private Crew findCrewOrThrow(Long crewId) {
        return crewRepository.findById(crewId)
                .orElseThrow(() -> new BusinessException(ErrorCode.CREW_NOT_FOUND));
    }
}
