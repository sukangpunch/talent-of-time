package com.example.talentoftime.period.dto;

import java.time.LocalTime;

public record PeriodCreateRequest(
        Integer periodNumber,
        LocalTime startTime,
        LocalTime endTime
) {
}
