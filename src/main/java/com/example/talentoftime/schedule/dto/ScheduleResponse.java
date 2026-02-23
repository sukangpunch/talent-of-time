package com.example.talentoftime.schedule.dto;

import com.example.talentoftime.schedule.domain.Schedule;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ScheduleResponse {

    private Long id;
    private LocalDate date;
    private int periodNumber;
    private LocalTime startTime;
    private LocalTime endTime;
    private int roomNumber;
    private String taskType;
    private Long crewId;
    private String crewName;

    public static ScheduleResponse from(Schedule schedule) {
        ScheduleResponse response = new ScheduleResponse();
        response.id = schedule.getId();
        response.date = schedule.getDate();
        response.periodNumber = schedule.getPeriod().getPeriodNumber();
        response.startTime = schedule.getPeriod().getStartTime();
        response.endTime = schedule.getPeriod().getEndTime();
        response.roomNumber = schedule.getClassroom().getRoomNumber();
        response.taskType = schedule.getTaskType().name();
        response.crewId = schedule.getCrew().getId();
        response.crewName = schedule.getCrew().getName();
        return response;
    }
}
