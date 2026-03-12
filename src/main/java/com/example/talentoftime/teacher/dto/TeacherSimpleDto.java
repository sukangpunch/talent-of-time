package com.example.talentoftime.teacher.dto;

import com.example.talentoftime.teacher.domain.Teacher;

public record TeacherSimpleDto(
        Long id,
        String name
) {
    public static TeacherSimpleDto from(Teacher teacher){
        return new TeacherSimpleDto(teacher.getId(), teacher.getName());
    }
}
