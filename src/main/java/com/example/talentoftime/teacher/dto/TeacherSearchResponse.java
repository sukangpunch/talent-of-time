package com.example.talentoftime.teacher.dto;

import com.example.talentoftime.teacher.domain.Teacher;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TeacherSearchResponse {

    private Long id;
    private String name;

    public static TeacherSearchResponse from(Teacher teacher) {
        TeacherSearchResponse response = new TeacherSearchResponse();
        response.id = teacher.getId();
        response.name = teacher.getName();
        return response;
    }
}
