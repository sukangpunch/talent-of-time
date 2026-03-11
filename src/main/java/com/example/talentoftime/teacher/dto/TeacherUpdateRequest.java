package com.example.talentoftime.teacher.dto;

import com.example.talentoftime.teacher.domain.ChalkType;
import com.example.talentoftime.teacher.domain.MicType;
import jakarta.validation.constraints.NotBlank;

public record TeacherUpdateRequest(
        @NotBlank
        String name,
        ChalkType chalkType,
        String chalkDetail,
        String eraserDetail,
        MicType micType,
        boolean hasPpt,
        String notes,
        String email
) {
}
