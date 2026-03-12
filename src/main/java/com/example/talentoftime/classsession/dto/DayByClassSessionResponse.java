package com.example.talentoftime.classsession.dto;

import java.util.List;

public record DayByClassSessionResponse(
    List<ClassSessionResponse> classSessionResponses
) {
}
