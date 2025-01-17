package com.group.pet.controller;

import com.group.pet.domain.dtos.ClientDTO;
import com.group.pet.domain.dtos.ScheduleDTO;
import com.group.pet.domain.dtos.SchedulePayload;
import com.group.pet.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static com.group.pet.utils.Constants.API_PATH;

@RestController
@RequestMapping(API_PATH)
public class ScheduleController {
    @Autowired
    private ScheduleService scheduleService;

    @GetMapping("/scheduling")
    public ResponseEntity<Map<LocalDate, Map<String, List<ScheduleDTO>>>> findAll(@RequestParam(value = "date") String date) {
        final List<ScheduleDTO> morningPeriod = scheduleService.findByDateAndPeriod(date, "manha");
        final List<ScheduleDTO> afternoomPeriod = scheduleService.findByDateAndPeriod(date, "tarde");
        final List<ScheduleDTO> nightPeriod = scheduleService.findByDateAndPeriod(date, "noite");

        final Map<String, List<ScheduleDTO>> dateByPeriod = Map.of(
                "Manhã", morningPeriod,
                "Tarde", afternoomPeriod,
                "Noite", nightPeriod
        );

        Map<LocalDate, Map<String, List<ScheduleDTO>>> body = Map.of(LocalDate.parse(date), dateByPeriod);
        return ResponseEntity.ok().body(body);
    }

    @GetMapping("/scheduling/employees")
    public ResponseEntity<Object> employeesAvailable(@RequestParam(value = "date") String date) {
        return ResponseEntity.ok().body(scheduleService.employeesAvailable(date));
    }

    @PostMapping("/schedule/create")
    public ResponseEntity<Void> create(@RequestBody SchedulePayload obj) {
        final ScheduleDTO insert = scheduleService.insert(obj);
        final URI uri = ServletUriComponentsBuilder.fromUriString("/api/schedule/{id}")
                .buildAndExpand(insert.id()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @DeleteMapping("/schedule/{id}")
    public ResponseEntity<Void> inactivate(@PathVariable Long id) {
        scheduleService.inactivate(id);
        return ResponseEntity.noContent().build();
    }
}
