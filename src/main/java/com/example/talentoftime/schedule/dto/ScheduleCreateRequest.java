package com.example.talentoftime.schedule.dto;

import com.example.talentoftime.common.domain.TaskType;

public record ScheduleCreateRequest(
        Long classSessionId,
        TaskType taskType
) {
}
