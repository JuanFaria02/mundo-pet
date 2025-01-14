package com.group.pet.service;

import com.group.pet.domain.Schedule;
import com.group.pet.domain.dtos.ScheduleDTO;
import com.group.pet.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScheduleService {
    @Autowired
    private ScheduleRepository scheduleRepository;

    public void insert(ScheduleDTO obj) {

    }

    public List<ScheduleDTO> findAll() {
       return scheduleRepository.findAll()
               .stream()
               .filter(Schedule::isActive)
               .map(schedule -> new ScheduleDTO(
                       schedule.getId(),
                       schedule.getClient().getName(),
                       schedule.getPet().getName(),
                       schedule.getUser().getNome(),
                       schedule.getDateShceduling(),
                       schedule.getTimeShceduling(),
                       schedule.getService(),
                       schedule.getPeriod()
                       ))
               .toList();
    }
}
