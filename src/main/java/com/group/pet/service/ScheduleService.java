package com.group.pet.service;

import com.group.pet.domain.Schedule;
import com.group.pet.domain.User;
import com.group.pet.domain.dtos.ScheduleDTO;
import com.group.pet.domain.dtos.SchedulePayload;
import com.group.pet.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Stream;

@Service
public class ScheduleService {
    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private ClientService clientService;

    @Autowired
    private UserService userService;

    private static final List<LocalTime> AVAILABLE_HOURS = Stream.iterate(
                    LocalTime.of(8, 0),
                    time -> time.plusMinutes(30))
            .limit(19)
            .toList();


    public void insert(SchedulePayload obj) {

    }

    public Map<String, Object> employeesAvailable(String date) {
        final List<Object> listMapVeterinarian = new ArrayList<>();
        final List<User> allVeterinarian = userService.findAllVeterinarian();
        final List<Schedule> allSchedules = scheduleRepository.findByDateShceduling(LocalDate.parse(date));

        for (final User user : allVeterinarian) {
            List<LocalTime> busyHours = allSchedules.stream()
                    .filter(schedule -> schedule.isActive() && schedule.getUser().equals(user))
                    .map(Schedule::getTimeShceduling)
                    .toList();

            List<String> availableHours = AVAILABLE_HOURS.stream()
                    .filter(Predicate.not(busyHours::contains))
                    .map(LocalTime::toString)
                    .toList();

            Object mapVeterinarianData = Map.of(
                    "veterinarianDocumentNumber", user.getDocumentNumber(),
                    "veterinarianName", user.getNome(),
                    "availableHours", availableHours
            );
            listMapVeterinarian.add(mapVeterinarianData);
        }

        return Map.of("availableEmployees", listMapVeterinarian);
    }

    public List<ScheduleDTO> findByDateAndPeriod(String date, String period) {
        LocalDate localDate = LocalDate.parse(date);

        final List<Schedule> byDateAndPeriod = scheduleRepository.findByDateShcedulingAndPeriod(localDate, period);
        return byDateAndPeriod
                .stream()
                .filter(Schedule::isActive)
                .map(schedule -> new ScheduleDTO(
                        schedule.getId(),
                        schedule.getClient().getName(),
                        schedule.getPet().getName(),
                        schedule.getUser().getNome(),
                        schedule.getDateShceduling().toString(),
                        schedule.getTimeShceduling().toString(),
                        schedule.getService(),
                        schedule.getPeriod()
                ))
                .toList();
    }
}
