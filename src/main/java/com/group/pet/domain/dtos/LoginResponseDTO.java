package com.group.pet.domain.dtos;

public record LoginResponseDTO(String token, String tokenExpireAt, String refreshToken, String refreshTokenExpireAt) {
}