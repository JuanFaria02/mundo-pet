package com.group.pet.domain.dtos;

import com.group.pet.domain.User;
import com.group.pet.domain.enums.UserType;

public class UserDTO {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private UserType type;
    private String documentNumber;
    private String password;

    public UserDTO(User user) {
        this.id = user.getId();
        this.name = user.getNome();
        this.email = user.getEmail();
        this.phone = user.getTelefone();
        this.type = user.getTipo();
        this.documentNumber = user.getDocumentNumber();
    }

    public UserDTO() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public UserType getType() {
        return type;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
