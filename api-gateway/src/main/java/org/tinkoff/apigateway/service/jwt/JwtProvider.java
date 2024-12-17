package org.tinkoff.apigateway.service.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
public class JwtProvider {

    @Value("${app.secret-key}")
    private String jwtSecret;
    @Getter
    private final int jwtExpirationMinutes = 10;
    @Getter
    private final int jwtExpirationDays = 30;

    public String generateToken(String username, boolean rememberMe) {
        log.info("Generating token for user: {}", username);
        int expirationTime = rememberMe ? jwtExpirationDays : jwtExpirationMinutes;
        Date expiration = new Date(System.currentTimeMillis() + expirationTime * 60 * 1000);
        String token = Jwts.builder()
                .subject(username)
                .expiration(expiration)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
        log.info("Token generated successfully for user: {}", username);
        return token;
    }

    public boolean validateToken(String token) {
        log.info("Validating token");
        try {
            Jwts.parser()
                    .setSigningKey(jwtSecret)
                    .build()
                    .parseSignedClaims(token);
            log.info("Token is valid");
            return true;
        } catch (Exception e) {
            log.warn("Token validation failed: {}", e.getMessage());
            return false;
        }
    }

    public String getUsernameFromToken(String token) {
        log.info("Extracting username from token");
        String username = Jwts.parser()
                .setSigningKey(jwtSecret)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("sub", String.class);
        log.info("Username extracted from token: {}", username);
        return username;
    }
}