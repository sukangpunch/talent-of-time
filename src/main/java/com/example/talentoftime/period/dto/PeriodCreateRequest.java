package com.example.talentoftime.period.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PeriodCreateRequest {

    @NotNull
    private Integer periodNumber;

    @NotNull
    private LocalTime startTime;

    @NotNull
    private LocalTime endTime;
}
