package com.group.pet.domain.enums;

public enum UserType {
    ATENDENTE("atendente"),
    ADMIN("admin");

    private String role;

    UserType(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
