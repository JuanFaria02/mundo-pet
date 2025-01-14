package com.group.pet.domain.dtos;

import java.time.LocalDate;
import java.time.LocalTime;

public record ScheduleDTO(Long id, String clientName, String petName, String userName, LocalDate date, LocalTime time, String service, String period) {
}
