package com.example.talentoftime.teacher.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "teacher")
public class Teacher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "teacher_id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "chalk_type")
    private ChalkType chalkType;

    @Column(name = "chalk_detail", columnDefinition = "TEXT")
    private String chalkDetail;

    @Column(name = "eraser_detail", columnDefinition = "TEXT")
    private String eraserDetail;

    @Enumerated(EnumType.STRING)
    @Column(name = "mic_type")
    private MicType micType;

    @Column(name = "has_ppt")
    private boolean hasPpt;

    @Column(name = "ppt_detail", columnDefinition = "TEXT")
    private String pptDetail;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "has_email")
    private boolean hasEmail;

    @Column(name = "email_detail", columnDefinition = "TEXT")
    private String emailDetail;

    public Teacher(
            String name,
            ChalkType chalkType,
            String chalkDetail,
            String eraserDetail,
            MicType micType,
            boolean hasPpt,
            String pptDetail,
            String notes,
            boolean hasEmail,
            String emailDetail)
    {
        this.name = name;
        this.chalkType = chalkType;
        this.chalkDetail = chalkDetail;
        this.eraserDetail = eraserDetail;
        this.micType = micType;
        this.hasPpt = hasPpt;
        this.pptDetail = pptDetail;
        this.notes = notes;
        this.hasEmail = hasEmail;
        this.emailDetail = emailDetail;
    }

    public void update(
            String name,
            ChalkType chalkType,
            String chalkDetail,
            String eraserDetail,
            MicType micType,
            boolean hasPpt,
            String pptDetail,
            String notes,
            boolean hasEmail,
            String emailDetail
    ) {
        this.name = name;
        this.chalkType = chalkType;
        this.chalkDetail = chalkDetail;
        this.eraserDetail = eraserDetail;
        this.micType = micType;
        this.hasPpt = hasPpt;
        this.pptDetail = pptDetail;
        this.notes = notes;
        this.hasEmail = hasEmail;
        this.emailDetail = emailDetail;
    }
}
