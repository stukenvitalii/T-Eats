package org.tinkoff.apigateway.service.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtProvider {

    @Value("${app.secret-key}")
    private String jwtSecret;
    @Getter
    private final int jwtExpirationMinutes = 10;
    @Getter
    private final int jwtExpirationDays = 30;

    public String generateToken(String username, boolean rememberMe) {
        int expirationTime = rememberMe ? jwtExpirationDays : jwtExpirationMinutes;
        Date expiration = new Date(System.currentTimeMillis() + expirationTime * 60 * 1000);
        return Jwts.builder()
                .subject(username)
                .expiration(expiration)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(jwtSecret)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getUsernameFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(jwtSecret)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("sub", String.class);
    }
}