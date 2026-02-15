package com.example.talentoftime.schedule.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ScheduleAutoAssignRequest {

    @NotNull
    private LocalDate date;

    @NotNull
    private Integer periodNumber;

    @NotNull
    private Long classroomId;
}
