package com.group.pet.domain;

import com.group.pet.domain.dtos.UserDTO;
import com.group.pet.domain.enums.UserType;
import jakarta.persistence.*;
import jdk.jfr.Timestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
public class User implements UserDetails, Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, columnDefinition = "text")
    private String senha;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String telefone;

    @Column(nullable = false, name = "tipo")
    @Enumerated(EnumType.STRING)
    private UserType tipo;

    @Column(nullable = false)
    private boolean ativo = true;

    @Column(nullable = false, name = "data_criacao")
    @Timestamp
    private LocalDateTime dataCriacao = LocalDateTime.now();

    @Column(name = "document_number")
    private String documentNumber;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.tipo == UserType.ADMIN) {
            return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_USER"));
        }

        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return ativo;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return ativo;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return senha;
    }

    public User() {}

    public User(Long id, String nome, String senha, String email, String telefone, UserType tipo, boolean ativo, String documentNumber) {
        this.id = id;
        this.nome = nome;
        this.senha = senha;
        this.email = email;
        this.telefone = telefone;
        this.tipo = tipo;
        this.ativo = ativo;
        this.documentNumber = documentNumber;
    }

    public void copyDto(UserDTO userDto) {
        this.id = userDto.getId();
        this.email = userDto.getEmail();
        this.nome = userDto.getNome();
        this.telefone = userDto.getTelefone();
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getSenha() {
        return senha;
    }

    public String getEmail() {
        return email;
    }

    public String getTelefone() {
        return telefone;
    }

    public UserType getTipo() {
        return tipo;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void changeActive() {
        ativo = !ativo;
    }
}
