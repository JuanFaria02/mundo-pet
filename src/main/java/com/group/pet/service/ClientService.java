package com.group.pet.service;

import com.group.pet.domain.Client;
import com.group.pet.domain.Pet;
import com.group.pet.domain.dtos.ClientDTO;
import com.group.pet.domain.dtos.PetDTO;
import com.group.pet.repository.ClientRepository;
import com.group.pet.service.exceptions.DatabaseException;
import com.group.pet.service.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientService {
    @Autowired
    private ClientRepository clientRepository;

    public void insert(ClientDTO client) {
        if (client.getId() != null) {
            throw new DatabaseException("Esse usuário já está cadastrado");
        }

        clientRepository.save(new Client(client.getId(), client.getName(), client.getPhone(), client.getDocumentNumber()));
    }

    public List<ClientDTO> findAll() {
        return clientRepository.findAll()
                .stream()
                .map(ClientDTO::new)
                .toList();
    }

    public ClientDTO findById(Long id) {
        Optional<Client> obj = clientRepository.findById(id);

        final Client client = obj.orElseThrow(()-> new ResourceNotFoundException(id));
        return new ClientDTO(client);
    }

    public void inactivate(Long id) {
        try {
            final Optional<Client> objClient = clientRepository.findById(id);

            final Client client = objClient.orElseThrow(() -> new ResourceNotFoundException(id));
            client.changeActive();
            clientRepository.save(client);
        }
        catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException(id);
        }
        catch (DataIntegrityViolationException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    public void update(ClientDTO obj) {
        if (obj.getId() == null) {
            throw new DatabaseException("id is required");
        }

        try {
            final Optional<Client> objClient = clientRepository.findById(obj.getId());

            final Client client = objClient.orElseThrow(() -> new ResourceNotFoundException(obj.getId()));
            updatePets(client.getPets(), obj.getPets(), client);
            client.copyDto(obj);
            clientRepository.save(client);
        } catch (RuntimeException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    private void updatePets(List<Pet> pets, List<PetDTO> newPets, Client client) {
        final List<PetDTO> petsToUpdate = newPets.stream()
                .filter(petDTO -> petDTO.id() != null)
                .toList();

        final List<PetDTO> petsToSave = newPets.stream()
                .filter(petDTO -> petDTO.id() == null)
                .toList();

        final List<Pet> petsToInactivate = pets.stream()
                .filter(pet -> newPets.stream().noneMatch(newPet -> newPet.id() != null && newPet.id().equals(pet.getId())))
                .toList();

        petsToUpdate.forEach(petDTO -> pets.stream()
                .filter(pet -> pet.getId().equals(petDTO.id()))
                .findFirst()
                .ifPresent(petWithSameId -> petWithSameId.copyDto(petDTO)));

        petsToSave.forEach(petDTO -> {
            final Pet pet = new Pet(null, petDTO.name(), petDTO.microchip(), petDTO.type());
            pet.setClient(client);
            pets.add(pet);
        });

        petsToInactivate.forEach(Pet::changeActive);
    }
}
