package com.example.talentoftime.crew.service;

import com.example.talentoftime.common.domain.TaskType;
import com.example.talentoftime.common.exception.BusinessException;
import com.example.talentoftime.common.exception.ErrorCode;
import com.example.talentoftime.count.domain.Count;
import com.example.talentoftime.count.repository.CountRepository;
import com.example.talentoftime.crew.domain.Crew;
import com.example.talentoftime.crew.domain.CrewType;
import com.example.talentoftime.crew.dto.CrewCreateRequest;
import com.example.talentoftime.crew.dto.CrewResponse;
import com.example.talentoftime.crew.dto.CrewUpdateRequest;
import com.example.talentoftime.crew.repository.CrewRepository;
import java.util.Arrays;
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
    private final CountRepository countRepository;

    @Transactional(readOnly = true)
    public List<CrewResponse> findAllCrews() {
        return crewRepository.findAll().stream()
                .map(CrewResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<CrewResponse> findCrewsByType(CrewType crewType) {
        return crewRepository.findByCrewType(crewType).stream()
                .map(CrewResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public CrewResponse findCrew(Long crewId) {
        Crew crew = findCrewOrThrow(crewId);
        return CrewResponse.from(crew);
    }

    private Crew findCrewOrThrow(Long crewId) {
        return crewRepository.findById(crewId)
                .orElseThrow(() -> new BusinessException(ErrorCode.CREW_NOT_FOUND));
    }

    @Transactional
    public CrewResponse createCrew(CrewCreateRequest request) {
        if (crewRepository.existsByName(request.getName())) {
            throw new BusinessException(ErrorCode.CREW_NAME_DUPLICATED);
        }

        Crew crew = Crew.create(request.getName(), request.getCrewType());
        crewRepository.save(crew);
        log.info("크루 생성 완료: name={}, crewType={}", crew.getName(), crew.getCrewType());

        List<Count> initialCounts = Arrays.stream(TaskType.values())
                .map(taskType -> Count.createInitial(crew, taskType))
                .toList();
        countRepository.saveAll(initialCounts);
        log.info("크루 count 초기화 완료: crewId={}", crew.getId());

        return CrewResponse.from(crew);
    }

    @Transactional
    public CrewResponse updateCrew(Long crewId, CrewUpdateRequest request) {
        Crew crew = findCrewOrThrow(crewId);

        if (request.getName() != null) {
            crew.updateName(request.getName());
        }
        if (request.getCrewType() != null) {
            crew.updateCrewType(request.getCrewType());
        }

        log.info("크루 수정 완료: crewId={}", crewId);
        return CrewResponse.from(crew);
    }

    @Transactional
    public void deleteCrew(Long crewId) {
        Crew crew = findCrewOrThrow(crewId);
        crewRepository.delete(crew);
        log.info("크루 삭제 완료: crewId={}", crewId);
    }
}
