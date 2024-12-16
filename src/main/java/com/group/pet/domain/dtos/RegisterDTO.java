package com.group.pet.domain.dtos;


import com.group.pet.domain.enums.UserType;

public record RegisterDTO(String name, String email, String password, String phone, UserType type, String documentNumber) {
}