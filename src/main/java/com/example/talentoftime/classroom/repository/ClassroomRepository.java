package com.example.talentoftime.classroom.repository;

import com.example.talentoftime.classroom.domain.Classroom;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassroomRepository extends JpaRepository<Classroom, Long> {

    boolean existsByRoomNumber(int roomNumber);

    Optional<Classroom> findByRoomNumber(int roomNumber);
}
