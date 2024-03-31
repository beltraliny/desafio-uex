package com.github.beltraliny.testeuex.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.github.beltraliny.testeuex.models.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    @Value("${api.security.token.key}")
    private String secretKey;

    public String createToken(User user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secretKey);
            Instant expirationTime = this.generateExpirationTime();

            String token = JWT.create()
                    .withIssuer("auth-api")
                    .withSubject(user.getUsername())
                    .withExpiresAt(expirationTime)
                    .sign(algorithm);

            return token;
        } catch (JWTCreationException jwtCreationException) {
            throw new RuntimeException("Authentication Error");
        }
    }

    public String validateTokenAndRetrieveUsername(String authorizationHeaderToken) {
        final String tokenPrefix = "Bearer ";

        try {
            if (authorizationHeaderToken == null) return null;

            String token = authorizationHeaderToken.replace(tokenPrefix, "");
            Algorithm algorithm = Algorithm.HMAC256(secretKey);

            String username = JWT.require(algorithm)
                    .withIssuer("auth-api")
                    .build()
                    .verify(token)
                    .getSubject();

            return username;
        } catch (JWTVerificationException jwtVerificationException) {
            return null;
        }
    }

    private Instant generateExpirationTime() {
        final long defaultExpirationTimeInHours = 1L;

        return LocalDateTime.now()
                .plusHours(defaultExpirationTimeInHours)
                .toInstant(ZoneOffset.of("-03:00"));
    }
}
