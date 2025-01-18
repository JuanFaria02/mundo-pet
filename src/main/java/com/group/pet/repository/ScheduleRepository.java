package com.group.pet.repository;

import com.group.pet.domain.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findByDateShcedulingAndPeriod(LocalDate date, String period);
    @Query("SELECT s FROM Schedule s WHERE s.dateShceduling = :date AND s.active = true")
    List<Schedule> findByDateShceduling(LocalDate date);
}
