package com.group.pet.service;

import com.group.pet.domain.Schedule;
import com.group.pet.domain.User;
import com.group.pet.domain.dtos.UserDTO;
import com.group.pet.domain.enums.UserType;
import com.group.pet.repository.UserRepository;
import com.group.pet.service.exceptions.DatabaseException;
import com.group.pet.service.exceptions.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

    public User findByDocumentNumber(String documentNumber) {
        try {
            return userRepository.findByDocumentNumber(documentNumber);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("User with email " + documentNumber + " not found");
        }
    }

    public User findUserPresentByDocumentNumberOrEmail(String documentNumber, String email) {
        if (findByEmail(email) != null) {
            return findByEmail(email);
        }

        if (findByDocumentNumber(documentNumber) != null) {
            return findByDocumentNumber(documentNumber);
        }

        return null;
    }

    public List<User> findAllVeterinarian() {
        return userRepository.findAll()
                .stream()
                .filter(user -> user.getTipo().equals(UserType.VETERINARIO) && user.isAtivo())
                .toList();
    }

    @Transactional
    public void insert(User user) {
        if (user.getId() != null) {
            throw new DatabaseException("Esse usuário já está cadastrado");
        }

        User userInactive = findUserPresentByDocumentNumberOrEmail(user.getDocumentNumber(), user.getEmail());

        if (userInactive != null) {
            user.changeActive();

            if (!user.isAtivo()) {
                throw new DatabaseException("Esse usuário já está cadastrado");
            }

            userRepository.save(userInactive);
            return;
        }

        userRepository.save(user);
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

    @Transactional
    public void inactivate(Long id) {
        try {
            final Optional<User> objUser = userRepository.findById(id);

            final User user = objUser.orElseThrow(() -> new ResourceNotFoundException(id));

            if (user.getEmail().equals("admin@gmail.com")) {
                throw new DataIntegrityViolationException("Não é possível deletar o admin");
            }
            user.changeActive();

            if (user.getScheduling() != null && user.getScheduling().isEmpty()) {
                user.getScheduling().forEach(Schedule::inactivate);
            }
            userRepository.save(user);
        }
        catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException(id);
        }
        catch (DataIntegrityViolationException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    @Transactional
    public UserDTO update(UserDTO obj, Long id) {
        try {
            final Optional<User> objUser = userRepository.findById(id);

            if (obj.getPassword() != null && !obj.getPassword().isBlank()) {
                String encryptedPassword = new BCryptPasswordEncoder().encode(obj.getPassword());
                obj.setPassword(encryptedPassword);
            }

            final User user = objUser.orElseThrow(() -> new ResourceNotFoundException(id));
            user.copyDto(obj);
            return new UserDTO(userRepository.save(user));
        } catch (RuntimeException e) {
            throw new DatabaseException(e.getMessage());
        }
    }
}
