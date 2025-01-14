package com.group.pet.service;

import com.group.pet.domain.Schedule;
import com.group.pet.domain.dtos.ScheduleDTO;
import com.group.pet.domain.dtos.SchedulePayload;
import com.group.pet.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ScheduleService {
    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private ClientService clientService;

    private UserService userService;

    public void insert(SchedulePayload obj) {

    }


    public List<ScheduleDTO> findByDateAndPeriod(String date, String period) {
        LocalDate localDate = LocalDate.parse(date);

        final List<Schedule> byDateAndPeriod = scheduleRepository.findByDateShcedulingAAndPeriod(localDate, period);
        return byDateAndPeriod
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
