package com.group.pet.domain.dtos;


import com.group.pet.domain.User;
import com.group.pet.domain.enums.UserType;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class UserDTO {
    private Long id;
    private String nome;
    private String email;
    private String telefone;
    private UserType tipo;
    private boolean ativo;
    private Collection<? extends GrantedAuthority> role;

    public UserDTO(User user) {
        this.id = user.getId();
        this.nome = user.getNome();
        this.email = user.getEmail();
        this.telefone = user.getTelefone();
        this.ativo = user.isAtivo();
        this.tipo = user.getTipo();
        this.role = user.getAuthorities();
    }

    public UserDTO() {
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getTelefone() {
        return telefone;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public Collection<? extends GrantedAuthority> getRole() {
        return role;
    }

    public UserType getTipo() {
        return tipo;
    }
}
