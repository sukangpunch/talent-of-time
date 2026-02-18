package com.example.talentoftime.period.fixture;

import com.example.talentoftime.period.domain.Period;
import com.example.talentoftime.period.repository.PeriodRepository;
import java.time.LocalTime;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.context.TestComponent;

@TestComponent
@RequiredArgsConstructor
public class PeriodFixture {

    private final PeriodRepository periodRepository;

    public Period 교시(int periodNumber, LocalTime startTime, LocalTime endTime) {
        Period period = Period.create(periodNumber, startTime, endTime);
        periodRepository.save(period);
        return period;
    }

    public Period 세팅_교시() {
        return 교시(0, LocalTime.of(8, 0), LocalTime.of(9, 0));
    }

    public Period 일교시() {
        return 교시(1, LocalTime.of(9, 0), LocalTime.of(11, 30));
    }

    public Period 이교시() {
        return 교시(2, LocalTime.of(11, 30), LocalTime.of(14, 0));
    }

    public Period 삼교시() {
        return 교시(3, LocalTime.of(14, 0), LocalTime.of(16, 30));
    }

    public Period 사교시() {
        return 교시(4, LocalTime.of(16, 30), LocalTime.of(19, 0));
    }
}
