package com.example.talentoftime.classroom.dto;

import com.example.talentoftime.classroom.domain.Classroom;

public record ClassroomResponse(
        Long id,
        int roomNumber
) {
    public static ClassroomResponse from(Classroom classroom) {
        return new ClassroomResponse(
                classroom.getId(),
                classroom.getRoomNumber()
        );
    }
}
