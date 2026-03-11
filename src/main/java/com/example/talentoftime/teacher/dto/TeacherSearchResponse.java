package com.example.talentoftime.teacher.dto;

import com.example.talentoftime.teacher.domain.Teacher;

public record TeacherSearchResponse(
        Long id,
        String name
) {
    public static TeacherSearchResponse from(Teacher teacher) {
        return new TeacherSearchResponse(teacher.getId(), teacher.getName());
    }
}
