package com.example.talentoftime.teacher.repository;

import com.example.talentoftime.teacher.domain.Teacher;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {

    List<Teacher> findAllByOrderByNameAsc();

    Optional<Teacher> findFirstByName(String name);

    List<Teacher> findByNameStartingWith(String name);
}
