package com.example.talentoftime.classsession.dto;

import java.time.LocalDate;

public record ClassSessionCreateRequest(
        LocalDate date,
        Integer periodNumber,
        Long classroomId,
        Long teacherId,
        String subject,
        String group,
        Integer inPersonCount
) {
}
