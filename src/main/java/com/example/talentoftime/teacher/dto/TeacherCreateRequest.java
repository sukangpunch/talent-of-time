package com.example.talentoftime.teacher.dto;

import com.example.talentoftime.teacher.domain.ChalkType;
import com.example.talentoftime.teacher.domain.MicType;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TeacherCreateRequest {

    @NotBlank
    private String name;
    private ChalkType chalkType;
    private String chalkDetail;
    private String eraserDetail;
    private MicType micType;
    private boolean hasPpt;
    private String pptDetail;
    private String notes;
    private String lectureEmailRecipient;
}
