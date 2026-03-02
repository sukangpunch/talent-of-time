package com.example.talentoftime.classsession.dto;

import com.example.talentoftime.classsession.domain.ClassStatus;
import java.time.LocalDate;

public record ClassSessionCreateRequest(
        LocalDate date,
        Integer periodNumber,
        Long classroomId,
        Long teacherId,
        String subject,
        String group,
        Integer inPersonCount,
        Integer onlineCount,
        ClassStatus classStatus
) {
}
