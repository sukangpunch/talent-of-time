package com.example.talentoftime.period.service;

import com.example.talentoftime.common.exception.BusinessException;
import com.example.talentoftime.common.exception.ErrorCode;
import com.example.talentoftime.period.domain.Period;
import com.example.talentoftime.period.dto.PeriodCreateRequest;
import com.example.talentoftime.period.dto.PeriodResponse;
import com.example.talentoftime.period.repository.PeriodRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class PeriodService {

    private final PeriodRepository periodRepository;

    @Transactional(readOnly = true)
    public List<PeriodResponse> findAllPeriods() {
        return periodRepository.findAll().stream()
                .map(PeriodResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public PeriodResponse findPeriod(Long periodId) {
        Period period = findPeriodOrThrow(periodId);
        return PeriodResponse.from(period);
    }

    private Period findPeriodOrThrow(Long periodId) {
        return periodRepository.findById(periodId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PERIOD_NOT_FOUND));
    }

    @Transactional
    public PeriodResponse createPeriod(PeriodCreateRequest request) {
        if (periodRepository.existsByPeriodNumber(request.periodNumber())) {
            throw new BusinessException(ErrorCode.PERIOD_NUMBER_DUPLICATED);
        }

        Period period = new Period(
                request.periodNumber(),
                request.startTime(),
                request.endTime()
        );
        periodRepository.save(period);
        log.info("교시 생성 완료: periodNumber={}", period.getPeriodNumber());

        return PeriodResponse.from(period);
    }

    @Transactional
    public void deletePeriod(Long periodId) {
        Period period = findPeriodOrThrow(periodId);
        periodRepository.delete(period);
        log.info("교시 삭제 완료: periodId={}", periodId);
    }
}
