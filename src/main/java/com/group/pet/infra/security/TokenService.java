package com.group.pet.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.group.pet.domain.User;
import com.group.pet.infra.security.expection.TokenException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;


@Service
public class TokenService {
    @Value("${api.security.token.secret}")
    private String secret;

    public String generateAccessToken(User user) {
        Instant expirationDate = Instant.now().plus(5, ChronoUnit.MINUTES);
        return generateToken(user, expirationDate);
    }

    public String generateRefreshToken(User user) {
        Instant expirationDate = Instant.now().plus(10, ChronoUnit.DAYS);
        return generateToken(user, expirationDate);
    }

    private String generateToken(User user, Instant expirationDate) {
        try{
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer("auth-api")
                    .withSubject(user.getEmail())
                    .withClaim("type", user.getTipo().name())
                    .withExpiresAt(expirationDate)
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Error while generating token", exception);
        }
    }

    public String validateToken(String token){
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("auth-api")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (TokenExpiredException exception) {
            throw new TokenException("TOKEN EXPIRED");
        }
        catch (JWTVerificationException exception) {
            return "";
        }
    }
}