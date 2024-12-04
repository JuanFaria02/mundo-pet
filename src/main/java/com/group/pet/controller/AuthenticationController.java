package com.group.pet.controller;

import com.group.pet.domain.User;
import com.group.pet.domain.dtos.AuthenticationDTO;
import com.group.pet.domain.dtos.LoginResponseDTO;
import com.group.pet.domain.dtos.RegisterDTO;
import com.group.pet.domain.dtos.UserDTO;
import com.group.pet.infra.security.TokenService;
import com.group.pet.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

import static com.group.pet.utils.Constants.API_PATH;
import static com.group.pet.utils.Constants.LOGIN_PATH;

@RestController
@RequestMapping
public class AuthenticationController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserService userService;
    @Autowired
    private TokenService tokenService;

    @PostMapping(LOGIN_PATH)
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Validated AuthenticationDTO data){
        UsernamePasswordAuthenticationToken emailPassword = new UsernamePasswordAuthenticationToken(data.email(), data.password());
        var auth = authenticationManager.authenticate(emailPassword);

        var token = tokenService.generateToken((User) auth.getPrincipal());

        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @PostMapping(API_PATH + "/usuario/criar")
    public ResponseEntity<UserDTO> register(@RequestBody @Validated RegisterDTO data){
        if (userService.findByEmail(data.email()) != null) return ResponseEntity.badRequest().build();

        String encryptedPassword = new BCryptPasswordEncoder().encode(data.senha());
        User newUser = new User(null, data.nome(), encryptedPassword, data.email(), data.telefone(), data.tipo(), true);

        userService.insert(newUser);

        final URI uri = ServletUriComponentsBuilder.fromUriString("/api/usuario/{id}")
                .buildAndExpand(newUser.getId()).toUri();

        return ResponseEntity.created(uri).body(new UserDTO(newUser));
    }
}