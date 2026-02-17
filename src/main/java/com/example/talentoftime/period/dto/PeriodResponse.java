package com.example.talentoftime.period.dto;

import com.example.talentoftime.period.domain.Period;
import java.time.LocalTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PeriodResponse {

    private Long id;
    private int periodNumber;
    private LocalTime startTime;
    private LocalTime endTime;

    public static PeriodResponse from(Period period) {
        PeriodResponse response = new PeriodResponse();
        response.id = period.getId();
        response.periodNumber = period.getPeriodNumber();
        response.startTime = period.getStartTime();
        response.endTime = period.getEndTime();
        return response;
    }
}
