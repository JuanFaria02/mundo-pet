package com.group.pet.controller;

import com.group.pet.domain.dtos.ScheduleDTO;
import com.group.pet.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.group.pet.utils.Constants.API_PATH;

@RestController
@RequestMapping(API_PATH)
public class ScheduleController {
    @Autowired
    private ScheduleService scheduleService;

    @GetMapping("/scheduling")
    public ResponseEntity<List<ScheduleDTO>> findAll(@RequestParam(value = "date", required = false) String date) {
        return ResponseEntity.ok().body(scheduleService.findAll());
    }
}
