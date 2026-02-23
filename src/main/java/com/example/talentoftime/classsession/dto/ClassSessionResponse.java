package com.example.talentoftime.classsession.dto;

import com.example.talentoftime.classsession.domain.ClassSession;
import com.example.talentoftime.teacher.dto.TeacherResponse;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ClassSessionResponse {

    private Long id;
    private LocalDate date;
    private int periodNumber;
    private int roomNumber;
    private TeacherResponse teacher;

    public static ClassSessionResponse from(ClassSession classSession) {
        ClassSessionResponse response = new ClassSessionResponse();
        response.id = classSession.getId();
        response.date = classSession.getDate();
        response.periodNumber = classSession.getPeriod().getPeriodNumber();
        response.roomNumber = classSession.getClassroom().getRoomNumber();
        if (classSession.getTeacher() != null) {
            response.teacher = TeacherResponse.from(classSession.getTeacher());
        }
        return response;
    }
}
