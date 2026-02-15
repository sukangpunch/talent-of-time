package com.example.talentoftime.classroom.repository;

import com.example.talentoftime.classroom.domain.Classroom;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClassroomRepository extends JpaRepository<Classroom, Long> {

    Optional<Classroom> findByRoomNumber(int roomNumber);
}
