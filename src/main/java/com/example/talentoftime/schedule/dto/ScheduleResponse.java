package com.example.talentoftime.schedule.dto;

import com.example.talentoftime.schedule.domain.Schedule;

public record ScheduleResponse(
        Long id,
        Long classSessionId,
        String taskType,
        Long crewId,
        String crewName
) {

    public static ScheduleResponse from(Schedule schedule) {
        return new ScheduleResponse(
                schedule.getId(),
                schedule.getClassSession().getId(),
                schedule.getTaskType().name(),
                schedule.getCrew().getId(),
                schedule.getCrew().getName()
        );
    }
}
