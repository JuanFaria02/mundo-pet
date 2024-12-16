package com.group.pet.domain.dtos;

import com.group.pet.domain.User;
import com.group.pet.domain.enums.UserType;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class UserDTO {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private UserType type;
    private String documentNumber;
    private Collection<? extends GrantedAuthority> role;

    public UserDTO(User user) {
        this.id = user.getId();
        this.name = user.getNome();
        this.email = user.getEmail();
        this.phone = user.getTelefone();
        this.type = user.getTipo();
        this.role = user.getAuthorities();
        this.documentNumber = user.getDocumentNumber();
    }

    public UserDTO() {
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getTelefone() {
        return phone;
    }

    public Collection<? extends GrantedAuthority> getRole() {
        return role;
    }

    public UserType getTipo() {
        return type;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }
}
