package com.group.pet.domain.dtos;

import java.time.LocalDate;
import java.time.LocalTime;

public record SchedulePayload(LocalDate date, String pet, String tutorCpf, String veterinarian, LocalTime time) {
}
