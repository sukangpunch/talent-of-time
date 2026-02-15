package com.example.talentoftime.period.repository;

import com.example.talentoftime.period.domain.Period;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PeriodRepository extends JpaRepository<Period, Long> {

    Optional<Period> findByPeriodNumber(int periodNumber);
}
