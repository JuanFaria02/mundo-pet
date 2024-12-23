package com.group.pet.service;

import com.group.pet.domain.User;
import com.group.pet.domain.dtos.UserDTO;
import com.group.pet.repository.UserRepository;
import com.group.pet.service.exceptions.DatabaseException;
import com.group.pet.service.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public List<UserDTO> findAll() {
        return userRepository.findAll()
                .stream()
                .filter(User::isAtivo)
                .map(UserDTO::new)
                .toList();
    }

    public UserDTO findById(Long id) {
        Optional<User> obj = userRepository.findById(id);

        final User user = obj.orElseThrow(()-> new ResourceNotFoundException(id));
        return new UserDTO(user);
    }

    public void inactivate(Long id) {
        try {
            final Optional<User> objUser = userRepository.findById(id);

            final User user = objUser.orElseThrow(() -> new ResourceNotFoundException(id));

            if (user.getEmail().equals("admin@gmail.com")) {
                throw new DataIntegrityViolationException("Não é possível deletar o admin");
            }
            user.changeActive();
            userRepository.save(user);
        }
        catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException(id);
        }
        catch (DataIntegrityViolationException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    public UserDTO update(UserDTO obj) {
        if (obj.getId() == null) {
            throw new DatabaseException("id is required");
        }

        try {
            final Optional<User> objUser = userRepository.findById(obj.getId());

            final User user = objUser.orElseThrow(() -> new ResourceNotFoundException(obj.getId()));
            user.copyDto(obj);
            return new UserDTO(userRepository.save(user));
        } catch (RuntimeException e) {
            throw new DatabaseException(e.getMessage());
        }
    }
}
