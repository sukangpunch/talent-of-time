package com.example.talentoftime.schedule.dto;

import com.example.talentoftime.common.domain.TaskType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ScheduleCreateRequest {

    @NotNull
    private Long classSessionId;

    @NotNull
    private TaskType taskType;
}
