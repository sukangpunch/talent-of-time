package com.example.talentoftime.teacher.dto;

import com.example.talentoftime.teacher.domain.ChalkType;
import com.example.talentoftime.teacher.domain.MicType;
import com.example.talentoftime.teacher.domain.Teacher;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TeacherResponse {

    private Long id;
    private String name;
    private ChalkType chalkType;
    private String chalkDetail;
    private String eraserDetail;
    private MicType micType;
    private boolean hasPpt;
    private String pptDetail;
    private String notes;
    private String lectureEmailRecipient;

    public static TeacherResponse from(Teacher teacher) {
        TeacherResponse response = new TeacherResponse();
        response.id = teacher.getId();
        response.name = teacher.getName();
        response.chalkType = teacher.getChalkType();
        response.chalkDetail = teacher.getChalkDetail();
        response.eraserDetail = teacher.getEraserDetail();
        response.micType = teacher.getMicType();
        response.hasPpt = teacher.isHasPpt();
        response.pptDetail = teacher.getPptDetail();
        response.notes = teacher.getNotes();
        response.lectureEmailRecipient = teacher.getLectureEmailRecipient();
        return response;
    }
}
