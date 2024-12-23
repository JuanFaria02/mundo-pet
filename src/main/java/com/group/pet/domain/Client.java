package com.group.pet.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.group.pet.domain.dtos.ClientDTO;
import jakarta.persistence.*;
import jdk.jfr.Timestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "client")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false, name = "document_number", unique = true)
    private String documentNumber;

    @Column(nullable = false)
    private boolean active = true;

    @Column(nullable = false, name = "created_at")
    @Timestamp
    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private List<Pet> pets = new ArrayList<>();

    public Client() {
    }

    public Client(Long id, String name, String phone, String documentNumber) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.documentNumber = documentNumber;
    }

    public void copyDto(ClientDTO clientDTO) {
        this.id = clientDTO.getId();
        this.phone = clientDTO.getPhone();
        this.name = clientDTO.getName();
        this.documentNumber = clientDTO.getDocumentNumber();
    }


    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public boolean isActive() {
        return active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public List<Pet> getPets() {
        return this.pets;
    }

    public void changeActive() {
        active = !active;
    }
}
