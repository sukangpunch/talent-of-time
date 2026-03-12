package com.example.talentoftime.classsession.dto;

import com.example.talentoftime.classsession.domain.ClassSession;
import com.example.talentoftime.teacher.domain.Teacher;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import java.time.LocalDate;

public record ClassSessionResponse(
        Long id,
        Long teacherId,
        String teacherName,
        int periodNumber,
        int roomNumber,
        String subject,
        String group,
        int inPersonCount,
        int onlineCount,
        String status,
        LocalDate date
) {
    public static ClassSessionResponse from(ClassSession classSession) {
        Teacher teacher = classSession.getTeacher();
        return new ClassSessionResponse(
                classSession.getId(),
                teacher != null ? teacher.getId() : null,
                teacher != null ? teacher.getName() : null,
                classSession.getPeriod().getPeriodNumber(),
                classSession.getClassroom().getRoomNumber(),
                classSession.getSubject(),
                classSession.getGroup(),
                classSession.getInPersonCount(),
                classSession.getOnlineCount(),
                classSession.getStatus().name(),
                classSession.getDate()
        );
    }
}
