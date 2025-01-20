package com.group.pet.repository;

import com.group.pet.domain.Client;
import com.group.pet.domain.Schedule;
import com.group.pet.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findByDateShcedulingAndPeriod(LocalDate date, String period);
    @Query("SELECT s FROM Schedule s WHERE s.dateShceduling = :date AND s.active = true")
    List<Schedule> findByDateShceduling(LocalDate date);
    @Query("SELECT s FROM Schedule s "+
            "WHERE s.dateShceduling = :date AND s.timeShceduling = :time AND (s.user = :user OR s.client = :client) AND s.active = true")
    Schedule findByDateShcedulingAndTimeScheduling(LocalDate date, LocalTime time, User user, Client client);
}
