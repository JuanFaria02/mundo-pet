package com.group.pet.domain;

import jakarta.persistence.*;
import jdk.jfr.Timestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

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
    @JoinColumn(name = "pet_id", foreignKey = @ForeignKey(name = "pet_fk_schedule"))
    private Pet pet;

    @Column(name = "date_scheduling")
    @Timestamp
    private LocalDate dateShceduling;

    @Column(name = "time_scheduling")
    @Timestamp
    private LocalTime timeShceduling;

    @Column(name = "period_scheduling")
    private String period;

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

    public Schedule(Client client, Pet pet, LocalDate dateShceduling, LocalTime timeShceduling, User user, String period, String service) {
        this.client = client;
        this.pet = pet;
        this.dateShceduling = dateShceduling;
        this.timeShceduling = timeShceduling;
        this.user = user;
        this.period = period;
        this.service = service;
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

    public LocalDate getDateShceduling() {
        return dateShceduling;
    }

    public LocalTime getTimeShceduling() {
        return timeShceduling;
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

    public String getPeriod() {
        return period;
    }

    public void changeActive() {
        active = !active;
    }
}
