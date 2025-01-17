package com.group.pet.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.group.pet.domain.dtos.PetDTO;
import jakarta.persistence.*;
import jdk.jfr.Timestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "pet")
public class Pet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String microchip;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false, name = "created_at")
    @Timestamp
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false)
    private boolean active = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", foreignKey = @ForeignKey(name = "client_fk_pet"))
    private Client client;

    public Pet() {
    }

    public Pet(Long id, String name, String microchip, String type) {
        this.id = id;
        this.name = name;
        this.microchip = microchip;
        this.type = type;
    }

    public void copyDto(PetDTO petDTO) {
        this.name = petDTO.name();
        this.microchip = petDTO.microchip();
        this.type = petDTO.type();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getMicrochip() {
        return microchip;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getType() {
        return type;
    }

    public boolean isActive() {
        return active;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void changeActive() {
        active = !active;
    }

    public void inactivate() {
        active = false;
    }
}
