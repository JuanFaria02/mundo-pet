package com.group.pet.controller;

import com.group.pet.domain.dtos.ScheduleDTO;
import com.group.pet.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<Map<LocalDate, Map<String, List<ScheduleDTO>>>> findAll(@RequestParam(value = "date", required = false) String date) {
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
}
