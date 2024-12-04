package com.group.pet.service;

import com.group.pet.domain.User;
import com.group.pet.repository.UserRepository;
import com.group.pet.service.exceptions.DatabaseException;
import com.group.pet.service.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User findByEmail(String email) {
        try {
            return userRepository.findByEmail(email);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("User with email " + email + " not found");
        }
    }

    public User insert(User user) {
        if (user.getId() != null) {
            throw new DatabaseException("Esse usuário já está cadastrado");
        }
        return userRepository.save(user);
    }
}
