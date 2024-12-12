package com.group.pet.controller;

import com.group.pet.domain.User;
import com.group.pet.domain.dtos.*;
import com.group.pet.infra.security.TokenService;
import com.group.pet.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

import static com.group.pet.utils.Constants.*;

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

        return ResponseEntity.ok(buildLoginResponse((User) auth.getPrincipal()));
    }

    @PostMapping(REFRESH_TOKEN_PATH)
    public ResponseEntity<LoginResponseDTO> authRefreshToken(@RequestBody RefreshTokenDTO refreshTokenDTO) {
        String email = tokenService.validateToken(refreshTokenDTO.refreshToken());

        User user = userService.findByEmail(email);
        return ResponseEntity.ok(buildLoginResponse(user));
    }

    @PostMapping(API_PATH + "/usuario/criar")
    public ResponseEntity<UserDTO> register(@RequestBody @Validated RegisterDTO data){
        if (userService.findByEmail(data.email()) != null) return ResponseEntity.badRequest().build();

        String encryptedPassword = new BCryptPasswordEncoder().encode(data.senha());
        User newUser = new User(null, data.nome(), encryptedPassword, data.email(), data.telefone(), data.tipo(), true);

        userService.insert(newUser);

        final URI uri = ServletUriComponentsBuilder.fromUriString("/api/usuario/{id}")
                .buildAndExpand(newUser.getId()).toUri();

        return ResponseEntity.created(uri).build();
    }

    private LoginResponseDTO buildLoginResponse(User user) {
        String accessToken = tokenService.generateAccessToken(user);
        String refreshToken = tokenService.generateRefreshToken(user);

        return new LoginResponseDTO(accessToken, TOKEN_EXPIRATION_DATE_TIME,
                refreshToken, REFRESH_TOKEN_EXPIRATION_DATE_TIME);
    }
}