package com.group.pet.controller;


import com.group.pet.domain.dtos.ClientDTO;
import com.group.pet.service.ClientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

import static com.group.pet.utils.Constants.API_PATH;

@RestController
@RequestMapping(API_PATH)
public class ClientController {
    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping(value = "/client")
    public ResponseEntity<List<ClientDTO>> findAll() {
        final List<ClientDTO> clients = clientService.findAll();
        return ResponseEntity.ok()
                .body(clients);
    }

    @GetMapping(value = "/client/{id}")
    public ResponseEntity<ClientDTO> findById(@PathVariable Long id) {
        final ClientDTO client = clientService.findById(id);
        return ResponseEntity.ok().body(client);
    }

    @DeleteMapping("/client/{id}")
    public ResponseEntity<Void> inactivate(@PathVariable Long id) {
        clientService.inactivate(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/client/{id}")
    public ResponseEntity<ClientDTO> update(@RequestBody ClientDTO obj) {
        clientService.update(obj);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/client/create")
    public ResponseEntity<ClientDTO> create(@RequestBody ClientDTO obj) {
        clientService.insert(obj);
        final URI uri = ServletUriComponentsBuilder.fromUriString("/api/client/{id}")
                .buildAndExpand(obj.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }
}