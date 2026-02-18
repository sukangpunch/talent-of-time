package com.example.talentoftime.classroom.fixture;

import com.example.talentoftime.classroom.domain.Classroom;
import com.example.talentoftime.classroom.repository.ClassroomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.context.TestComponent;

@TestComponent
@RequiredArgsConstructor
public class ClassroomFixture {

    private final ClassroomRepository classroomRepository;

    public Classroom 강의실(int roomNumber) {
        Classroom classroom = Classroom.create(roomNumber);
        classroomRepository.save(classroom);
        return classroom;
    }

    public Classroom 육백삼호() {
        return 강의실(603);
    }

    public Classroom 육백사호() {
        return 강의실(604);
    }
}
