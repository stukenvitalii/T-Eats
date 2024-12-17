package org.tinkoff.apigateway.service.jwt;

import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class JwtProviderTest {

    private JwtProvider jwtProvider;

    private final String testSecretKey = "1d4e8f94cc1b991c3c2cccab95189d44e8d52cd72d2ae6875627180cf0a5aa400f201d40886d79b82cca98b5473d86685940b8066aaa319abc241d349c54f3f3eb6e2abfa3cb4a5723a181ddf5cb245a6b31b8569cf85cc7c1dd970d6e05a3290582504398ee1987409cf46ec49478efe576518dcb96bef94cbd9e43a2494a739713f3fc94fe1fe58a6594c83a7fda148998e9d25965f14870db732f123f4de422008d1585d167ddc99351d76e4aee241c52bce175410ce8a69f04e9653ad967f27ffa73e4fed0d181a49e59df31f1ebf18dcd577026bcdd11598e73940715741552ab6c7bcedcc70b2e39532934686336692a456c8f24939325bfb200a42cb04f0df23f6754f0da606a5d78e49df536686c548fde80157cc0e79efe79cc8d96e01a22cc120eef21f62d871d3c050b62b70d7c73093cfa10c210e445cbc0829b35015061f6ff58834dc3a833bad5824cfc1e1872d49149e7aa0c4cd9f730363f24cc7aa0271e4ed0e1422cc200e837b2801d7c79e34eac6cdc9447f2e3a8c8b2c345c4419393efeb5d895a4b5d0e3c6c4df0a418dd80252a4b7af362bfe129e9f3259813b133d894ea2cdb1c6628c2ca68e0415e54494913d76f094d5eb75881caf63e26a50be7f5dc54de2084c0b088eb6f0295dd84fed45b02f1df51a0919c3d95fd9b043d6101e5917281b926235ee08d45752fe6f196a0c686cc86b4973e";

    @BeforeEach
    void setUp() {
        jwtProvider = new JwtProvider();
        ReflectionTestUtils.setField(jwtProvider, "jwtSecret", testSecretKey);
    }

    @Test
    void generateTokenWithRememberMe() {
        String token = jwtProvider.generateToken("testUser", true);
        String username = Jwts.parser()
                .setSigningKey(testSecretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("sub", String.class);
        assertEquals("testUser", username);
    }

    @Test
    void generateTokenWithoutRememberMe() {
        String token = jwtProvider.generateToken("testUser", false);
        String username = Jwts.parser()
                .setSigningKey(testSecretKey)
                .build()
                .parseSignedClaims(token).getPayload().get("sub", String.class);

        assertEquals("testUser", username);
    }

    @Test
    void validateTokenWithValidToken() {
        String token = jwtProvider.generateToken("testUser", false);
        assertTrue(jwtProvider.validateToken(token));
    }

    @Test
    void validateTokenWithInvalidToken() {
        String token = "invalidToken";
        assertFalse(jwtProvider.validateToken(token));
    }

    @Test
    void getUsernameFromValidToken() {
        String token = jwtProvider.generateToken("testUser", false);
        String username = jwtProvider.getUsernameFromToken(token);
        assertEquals("testUser", username);
    }

    @Test
    void getUsernameFromInvalidToken() {
        String token = "invalidToken";
        assertThrows(Exception.class, () -> jwtProvider.getUsernameFromToken(token));
    }
}