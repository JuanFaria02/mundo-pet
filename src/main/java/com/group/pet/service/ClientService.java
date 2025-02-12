package com.group.pet.service;

import com.group.pet.domain.Client;
import com.group.pet.domain.Pet;
import com.group.pet.domain.Schedule;
import com.group.pet.domain.dtos.ClientDTO;
import com.group.pet.domain.dtos.PetDTO;
import com.group.pet.repository.ClientRepository;
import com.group.pet.repository.PetRepository;
import com.group.pet.service.exceptions.DatabaseException;
import com.group.pet.service.exceptions.ResourceNotFoundException;
import jakarta.transaction.Transactional;
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
    @Autowired
    private PetRepository petRepository;

    public Client findByDocumentNumber(String documentNumber) {
        try {
            return clientRepository.findByDocumentNumber(documentNumber);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("User with email " + documentNumber + " not found");
        }
    }

    @Transactional
    public void insert(ClientDTO client) {
        if (findByDocumentNumber(client.getDocumentNumber()) != null) {
            Client clientInactive = findByDocumentNumber(client.getDocumentNumber());
            clientInactive.changeActive();

            if (!clientInactive.isActive()) {
                throw new DatabaseException("Esse usuário já está cadastrado");
            }

            clientRepository.save(clientInactive);
            updatePets(clientInactive.getPets(), client.getPets(), clientInactive);
            clientRepository.save(clientInactive);
            return;
        }
        final Client newClient = new Client(client.getId(), client.getName(), client.getPhone(), client.getDocumentNumber());

        clientRepository.save(newClient);

        client.getPets().forEach(petDTO -> {
            final Pet pet = new Pet(null, petDTO.name(), petDTO.microchip(), petDTO.type());
            pet.setClient(newClient);
            if (petRepository.findByMicrochip(pet.getMicrochip()) != null) {
                throw new DatabaseException("Microchip já está cadastrado");
            }
            newClient.getPets().add(pet);
        });

        clientRepository.save(newClient);
    }

    public List<ClientDTO> findAll() {
        return clientRepository.findAll()
                .stream()
                .filter(Client::isActive)
                .map(ClientDTO::new)
                .toList();
    }

    public ClientDTO findById(Long id) {
        Optional<Client> obj = clientRepository.findById(id);

        final Client client = obj.orElseThrow(()-> new ResourceNotFoundException(id));
        return new ClientDTO(client);
    }

    @Transactional
    public void inactivate(Long id) {
        try {
            final Optional<Client> objClient = clientRepository.findById(id);

            final Client client = objClient.orElseThrow(() -> new ResourceNotFoundException(id));
            client.changeActive();
            client.getPets().forEach(Pet::inactivate);
            if (client.getScheduling() != null && client.getScheduling().isEmpty()) {
                client.getScheduling().forEach(Schedule::inactivate);
            }

            clientRepository.save(client);
        }
        catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException(id);
        }
        catch (DataIntegrityViolationException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    @Transactional
    public ClientDTO update(ClientDTO obj, Long id) {
        try {
            final Optional<Client> objClient = clientRepository.findById(id);

            final Client client = objClient.orElseThrow(() -> new ResourceNotFoundException(id));
            updatePets(client.getPets(), obj.getPets(), client);
            client.copyDto(obj);
            Client updatedClient = clientRepository.save(client);
            return new ClientDTO(updatedClient);
        } catch (RuntimeException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    private void updatePets(List<Pet> pets, List<PetDTO> newPets, Client client) {
        final List<String> microchips = pets.stream().map(Pet::getMicrochip).toList();

        final List<PetDTO> petsToUpdate = newPets.stream()
                .filter(petDTO -> microchips.contains(petDTO.microchip()))
                .toList();

        final List<PetDTO> petsToSave = newPets.stream()
                .filter(petDTO -> !microchips.contains(petDTO.microchip()))
                .toList();

        final List<Pet> petsToInactivate = pets.stream()
                .filter(pet -> newPets.stream().noneMatch(newPet -> newPet.microchip().equals(pet.getMicrochip())))
                .toList();

        if (!petsToInactivate.isEmpty()) {
            petsToInactivate.stream()
                    .filter(pet -> pet.getScheduling() != null && !pet.getScheduling().isEmpty())
                    .forEach(pet -> pet.getScheduling().forEach(Schedule::inactivate));
        }

        petsToUpdate.forEach(petDTO -> pets.stream()
                .filter(pet -> pet.getMicrochip().equals(petDTO.microchip()))
                .findFirst()
                .ifPresent(petWithSameId -> petRepository.findByMicrochip(petDTO.microchip()).copyDto(petDTO)));

        petsToSave.forEach(petDTO -> {
            final Pet pet = new Pet(null, petDTO.name().toLowerCase(), petDTO.microchip(), petDTO.type());
            pet.setClient(client);
            pets.add(pet);
        });

        petsToInactivate.forEach(Pet::inactivate);
    }
}
