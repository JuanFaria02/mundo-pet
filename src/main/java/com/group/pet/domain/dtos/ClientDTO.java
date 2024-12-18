package com.group.pet.domain.dtos;

import com.group.pet.domain.Client;

import java.util.List;

public class ClientDTO {
    private Long id;
    private String name;
    private String phone;
    private String documentNumber;
    private List<PetDTO> pets;

    public ClientDTO(Client client) {
        this.id = client.getId();
        this.name = client.getName();
        this.phone = client.getPhone();
        this.documentNumber = client.getDocumentNumber();
        this.pets = client.getPets().stream()
                .map(pet -> new PetDTO(pet.getId(), pet.getName(), pet.getMicrochip(), pet.getType()))
                .toList();
    }

    public ClientDTO() {
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

    public List<PetDTO> getPets() {
        return pets;
    }
}
