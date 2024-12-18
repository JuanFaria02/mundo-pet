package com.group.pet.controller;


import com.group.pet.domain.dtos.UserDTO;
import com.group.pet.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.group.pet.utils.Constants.API_PATH;

@RestController
@RequestMapping(API_PATH)
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping(value = "/users")
    public ResponseEntity<List<UserDTO>> findAll() {
        final List<UserDTO> users = userService.findAll();
        return ResponseEntity.ok()
                .body(users);
    }

    @GetMapping(value = "/user/{id}")
    public ResponseEntity<UserDTO> findById(@PathVariable Long id) {
        final UserDTO user = userService.findById(id);
        return ResponseEntity.ok().body(user);
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<Void> inactivate(@PathVariable Long id) {
        userService.inactivate(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/user/{id}")
    public ResponseEntity<UserDTO> update(@RequestBody UserDTO obj) {
        obj = userService.update(obj);
        return ResponseEntity.noContent().build();
    }
}