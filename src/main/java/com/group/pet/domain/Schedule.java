package com.group.pet.domain;

import jakarta.persistence.*;
import jdk.jfr.Timestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "schedule")
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", foreignKey = @ForeignKey(name = "client_fk_shcedule"))
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id", foreignKey = @ForeignKey(name = "pet_fk_shcedule"))
    private Pet pet;

    @Column(name = "date_scheduling")
    @Timestamp
    private LocalDateTime dateShceduling;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "user_fk_shcedule"))
    private User user;

    @Column
    private String service;

    @Column(nullable = false)
    private boolean active = true;


    private LocalDateTime createdAt = LocalDateTime.now();

    public Schedule() {
    }

    public Schedule(Client client, Pet pet, LocalDateTime dateShceduling, User user) {
        this.client = client;
        this.pet = pet;
        this.dateShceduling = dateShceduling;
        this.user = user;
    }

    public long getId() {
        return id;
    }

    public Pet getPet() {
        return pet;
    }

    public Client getClient() {
        return client;
    }

    public LocalDateTime getDateShceduling() {
        return dateShceduling;
    }

    public User getUser() {
        return user;
    }

    public String getService() {
        return service;
    }

    public boolean isActive() {
        return active;
    }

    public void changeActive() {
        active = !active;
    }
}
