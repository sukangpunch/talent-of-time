package com.example.talentoftime.teacher.dto;

import com.example.talentoftime.teacher.domain.ChalkType;
import com.example.talentoftime.teacher.domain.MicType;
import com.example.talentoftime.teacher.domain.Teacher;

public record TeacherResponse(
        Long id,
        String name,
        ChalkType chalkType,
        String chalkDetail,
        String eraserDetail,
        MicType micType,
        boolean hasPpt,
        String notes,
        String email
) {
    public static TeacherResponse from(Teacher teacher) {
        return new TeacherResponse(
                teacher.getId(),
                teacher.getName(),
                teacher.getChalkType(),
                teacher.getChalkDetail(),
                teacher.getEraserDetail(),
                teacher.getMicType(),
                teacher.isHasPpt(),
                teacher.getNotes(),
                teacher.getEmail()
        );
    }
}
