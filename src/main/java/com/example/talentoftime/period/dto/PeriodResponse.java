package com.example.talentoftime.period.dto;

import com.example.talentoftime.period.domain.Period;
import java.time.LocalTime;

public record PeriodResponse(
        Long id,
        int periodNumber,
        LocalTime startTime,
        LocalTime endTime
) {

    public static PeriodResponse from(Period period) {
        return new PeriodResponse(
                period.getId(),
                period.getPeriodNumber(),
                period.getStartTime(),
                period.getEndTime()
        );
    }
}
