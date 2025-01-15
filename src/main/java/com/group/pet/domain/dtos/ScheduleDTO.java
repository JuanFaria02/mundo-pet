package com.group.pet.domain.dtos;


public record ScheduleDTO(Long id, String clientName, String petName, String userName, String date, String time, String service, String period) {
}
