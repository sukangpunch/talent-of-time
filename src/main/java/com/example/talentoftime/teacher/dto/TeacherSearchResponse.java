package com.example.talentoftime.teacher.dto;


import java.util.List;

public record TeacherSearchResponse(
        List<TeacherSimpleDto> teacherSimpleDtoList
) {

}
