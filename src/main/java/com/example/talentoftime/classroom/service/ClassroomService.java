package com.example.talentoftime.classroom.service;

import com.example.talentoftime.classroom.domain.Classroom;
import com.example.talentoftime.classroom.dto.ClassroomCreateRequest;
import com.example.talentoftime.classroom.dto.ClassroomResponse;
import com.example.talentoftime.classroom.repository.ClassroomRepository;
import com.example.talentoftime.common.exception.BusinessException;
import com.example.talentoftime.common.exception.ErrorCode;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClassroomService {

    private final ClassroomRepository classroomRepository;

    @Transactional(readOnly = true)
    public List<ClassroomResponse> findAllClassrooms() {
        return classroomRepository.findAll().stream()
                .map(ClassroomResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public ClassroomResponse findClassroom(Long classroomId) {
        Classroom classroom = findClassroomOrThrow(classroomId);
        return ClassroomResponse.from(classroom);
    }

    private Classroom findClassroomOrThrow(Long classroomId) {
        return classroomRepository.findById(classroomId)
                .orElseThrow(() -> new BusinessException(ErrorCode.CLASSROOM_NOT_FOUND));
    }

    @Transactional
    public ClassroomResponse createClassroom(ClassroomCreateRequest request) {
        if (classroomRepository.existsByRoomNumber(request.getRoomNumber())) {
            throw new BusinessException(ErrorCode.CLASSROOM_ROOM_NUMBER_DUPLICATED);
        }

        Classroom classroom = Classroom.create(request.getRoomNumber());
        classroomRepository.save(classroom);
        log.info("강의실 생성 완료: roomNumber={}", classroom.getRoomNumber());

        return ClassroomResponse.from(classroom);
    }

    @Transactional
    public void deleteClassroom(Long classroomId) {
        Classroom classroom = findClassroomOrThrow(classroomId);
        classroomRepository.delete(classroom);
        log.info("강의실 삭제 완료: classroomId={}", classroomId);
    }
}
