package com.example.talentoftime.classroom.dto;

import com.example.talentoftime.classroom.domain.Classroom;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ClassroomResponse {

    private Long id;
    private int roomNumber;

    public static ClassroomResponse from(Classroom classroom) {
        ClassroomResponse response = new ClassroomResponse();
        response.id = classroom.getId();
        response.roomNumber = classroom.getRoomNumber();
        return response;
    }
}
