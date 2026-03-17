package com.example.talentoftime.classsession.dto;

public record ClassSessionParseResultResponse(
        String dayOfWeek,
        String period,
        String roomName,
        String teacherName,
        String className,
        String classSection,
        Integer offlineStudentCount,
        Integer onlineStudentCount) {
}
