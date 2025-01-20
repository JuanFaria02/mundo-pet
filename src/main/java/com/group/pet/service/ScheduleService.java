package com.group.pet.service;

import com.group.pet.domain.Client;
import com.group.pet.domain.Pet;
import com.group.pet.domain.Schedule;
import com.group.pet.domain.User;
import com.group.pet.domain.dtos.ScheduleDTO;
import com.group.pet.domain.dtos.SchedulePayload;
import com.group.pet.repository.ScheduleRepository;
import com.group.pet.service.exceptions.DatabaseException;
import com.group.pet.service.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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


    public ScheduleDTO insert(SchedulePayload obj) {
        final Client client = clientService.findByDocumentNumber(obj.clientDocumentNumber());
        final User user = userService.findByDocumentNumber(obj.veterinarianDocumentNumber());

        if (client == null || user == null) {
            throw new ResourceNotFoundException("Client not found");
        }

        if (obj.pet() == null || obj.pet().isBlank()) {
            throw new ResourceNotFoundException("Pet not found");
        }

        final Schedule scheduleAtSameTime = scheduleRepository.findByDateShcedulingAndTimeScheduling(obj.date(), obj.time(), user);

        if (scheduleAtSameTime != null) {
            throw new DatabaseException("Employee busy");
        }

        try {
            final Pet pet = client.getPets()
                    .stream()
                    .filter(p -> p.getName().equals(obj.pet().toLowerCase()))
                    .findFirst()
                    .orElse(null);

            final Schedule schedule = new Schedule(client, pet, obj.date(), obj.time(), user, getPeriod(obj.time()), obj.service());
            final Schedule scheduleSave = scheduleRepository.save(schedule);

            return new ScheduleDTO(scheduleSave.getId(), scheduleSave.getClient().getName(),
                    scheduleSave.getPet().getName(),
                    scheduleSave.getUser().getUsername(),
                    scheduleSave.getDateShceduling().toString(), scheduleSave.getTimeShceduling().toString(),
                    scheduleSave.getService(), scheduleSave.getPeriod());
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException(e.getMessage());
        } catch (EmptyResultDataAccessException | ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
    }

    public String getPeriod(LocalTime time) {
        if (time.isAfter(LocalTime.of(17, 0))) {
            return "night";
        }
        if (time.isAfter(LocalTime.of(12, 0))) {
            return "afternoom";
        }
        if (time.isAfter(LocalTime.of(8, 0))) {
            return "morning";
        }

        return null;
    }

    public void inactivate(Long id) {
        try {
            final Optional<Schedule> objSchedule = scheduleRepository.findById(id);

            final Schedule schedule = objSchedule.orElseThrow(() -> new ResourceNotFoundException(id));

            schedule.changeActive();
            scheduleRepository.save(schedule);
        }
        catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException(id);
        }
        catch (DataIntegrityViolationException e) {
            throw new DatabaseException(e.getMessage());
        }
    }


    public Map<String, Object> employeesAvailable(String date) {
        final List<Object> listMapVeterinarian = new ArrayList<>();
        final List<User> allVeterinarianActive = userService.findAllVeterinarian();
        final List<Schedule> allSchedulesActive = scheduleRepository.findByDateShceduling(LocalDate.parse(date));

        for (final User user : allVeterinarianActive) {
            List<LocalTime> busyHours = allSchedulesActive.stream()
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
