package com.group.pet.domain.dtos;


import com.group.pet.domain.enums.UserType;

public record RegisterDTO(String nome, String email, String senha, String telefone, UserType tipo) {
}