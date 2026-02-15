package com.example.talentoftime.schedule.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ScheduleSwapRequest {

    @NotNull
    private Long scheduleIdA;

    @NotNull
    private Long scheduleIdB;
}
