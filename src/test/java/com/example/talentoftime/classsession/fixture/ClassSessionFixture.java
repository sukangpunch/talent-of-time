package com.example.talentoftime.classsession.fixture;

import com.example.talentoftime.classsession.domain.ClassSession;
import com.example.talentoftime.classsession.dto.ClassSessionBulkCreateRequest;
import com.example.talentoftime.classsession.dto.ClassSessionCreateRequest;
import com.example.talentoftime.classsession.repository.ClassSessionRepository;
import com.example.talentoftime.classroom.domain.Classroom;
import com.example.talentoftime.period.domain.Period;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.test.util.ReflectionTestUtils;

@TestComponent
@RequiredArgsConstructor
public class ClassSessionFixture {

    private final ClassSessionRepository classSessionRepository;

    public ClassSession 수업일정(LocalDate date, Period period, Classroom classroom) {
        ClassSession session = ClassSession.create(date, period, classroom);
        classSessionRepository.save(session);
        return session;
    }

    public ClassSessionCreateRequest 단건_요청(LocalDate date, int periodNumber, Long classroomId) {
        ClassSessionCreateRequest request = new ClassSessionCreateRequest();
        ReflectionTestUtils.setField(request, "date", date);
        ReflectionTestUtils.setField(request, "periodNumber", periodNumber);
        ReflectionTestUtils.setField(request, "classroomId", classroomId);
        return request;
    }

    public ClassSessionBulkCreateRequest 일괄_요청(List<ClassSessionCreateRequest> sessions) {
        ClassSessionBulkCreateRequest request = new ClassSessionBulkCreateRequest();
        ReflectionTestUtils.setField(request, "sessions", sessions);
        return request;
    }
}
