package com.example.talentoftime.count.service;

import com.example.talentoftime.common.domain.TaskType;
import com.example.talentoftime.common.exception.BusinessException;
import com.example.talentoftime.common.exception.ErrorCode;
import com.example.talentoftime.count.domain.Count;
import com.example.talentoftime.count.dto.CountResponse;
import com.example.talentoftime.count.repository.CountRepository;
import com.example.talentoftime.crew.domain.Crew;
import com.example.talentoftime.crew.repository.CrewRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CountService {

    private final CountRepository countRepository;
    private final CrewRepository crewRepository;

    @Transactional(readOnly = true)
    public List<CountResponse> findAllCounts() {
        return countRepository.findAll().stream()
                .map(CountResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<CountResponse> findCountsByCrew(Long crewId) {
        Crew crew = crewRepository.findById(crewId)
                .orElseThrow(() -> new BusinessException(ErrorCode.CREW_NOT_FOUND));
        return countRepository.findByCrew(crew).stream()
                .map(CountResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<CountResponse> findCountsByTaskType(TaskType taskType) {
        return countRepository.findByTaskType(taskType).stream()
                .map(CountResponse::from)
                .toList();
    }

    @Transactional
    public void resetCountsByCrew(Long crewId) {
        Crew crew = crewRepository.findById(crewId)
                .orElseThrow(() -> new BusinessException(ErrorCode.CREW_NOT_FOUND));
        List<Count> counts = countRepository.findByCrew(crew);
        counts.forEach(Count::reset);
        log.info("크루 count 초기화 완료: crewId={}", crewId);
    }

    @Transactional
    public void resetAllCounts() {
        List<Count> counts = countRepository.findAll();
        counts.forEach(Count::reset);
        log.info("전체 count 초기화 완료");
    }
}
