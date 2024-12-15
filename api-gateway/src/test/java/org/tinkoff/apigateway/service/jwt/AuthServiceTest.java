package org.tinkoff.apigateway.service.jwt;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.tinkoff.apigateway.client.UserServiceClient;
import org.tinkoff.apigateway.dto.Role;
import org.tinkoff.apigateway.dto.UserDto;
import org.tinkoff.apigateway.dto.auth.request.LoginRequest;
import org.tinkoff.apigateway.dto.auth.request.RegisterRequest;
import org.tinkoff.apigateway.dto.auth.request.ResetPasswordRequest;
import org.tinkoff.apigateway.dto.auth.response.JwtResponse;

class AuthServiceTest {

    @Mock
    private JwtProvider jwtProvider;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserServiceClient userServiceClient;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerWithValidRequest() {
        RegisterRequest request = new RegisterRequest("username", "password", "email", "firstName", "lastName");
        when(userServiceClient.isUsernameTaken(request.getUsername())).thenReturn(false);
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");
        when(jwtProvider.generateToken(request.getUsername(), false)).thenReturn("jwtToken");

        ResponseEntity<?> response = authService.register(request);

        assertEquals(200, response.getStatusCode().value());
        assertInstanceOf(JwtResponse.class, response.getBody());
        assertEquals("jwtToken", ((JwtResponse) response.getBody()).getToken());
    }

    @Test
    void registerWithTakenUsername() {
        RegisterRequest request = new RegisterRequest("username", "password", "email", "firstName", "lastName");
        when(userServiceClient.isUsernameTaken(request.getUsername())).thenReturn(true);

        ResponseEntity<?> response = authService.register(request);

        assertEquals(400, response.getStatusCode().value());
        assertEquals("Username already taken", response.getBody());
    }

    @Test
    void authenticateWithValidCredentials() {
        LoginRequest request = new LoginRequest("username", "password", false);
        UserDto user = new UserDto("username", "password", Role.USER);
        when(userServiceClient.getUserByUsername(request.getUsername())).thenReturn(user);
        when(passwordEncoder.matches(request.getPassword(), user.getPassword())).thenReturn(true);
        when(jwtProvider.generateToken(user.getUsername(), request.isRememberMe())).thenReturn("jwtToken");

        ResponseEntity<?> response = authService.authenticate(request);

        assertEquals(200, response.getStatusCode().value());
        assertInstanceOf(JwtResponse.class, response.getBody());
        assertEquals("jwtToken", ((JwtResponse) response.getBody()).getToken());
    }

    @Test
    void authenticateWithInvalidCredentials() {
        LoginRequest request = new LoginRequest("username", "password", false);
        UserDto user = new UserDto("username", "password", Role.USER);
        when(userServiceClient.getUserByUsername(request.getUsername())).thenReturn(user);
        when(passwordEncoder.matches(request.getPassword(), user.getPassword())).thenReturn(false);

        ResponseEntity<?> response = authService.authenticate(request);

        assertEquals(400, response.getStatusCode().value());
        assertEquals("Invalid credentials", response.getBody());
    }

    @Test
    void authenticateWithNonExistentUser() {
        LoginRequest request = new LoginRequest("username", "password", false);
        when(userServiceClient.getUserByUsername(request.getUsername())).thenReturn(null);

        ResponseEntity<?> response = authService.authenticate(request);

        assertEquals(400, response.getStatusCode().value());
        assertEquals("User not found", response.getBody());
    }

    @Test
    void logoutWithValidToken() {
        String token = "Bearer validToken";
        when(jwtProvider.validateToken("validToken")).thenReturn(true);

        assertDoesNotThrow(() -> authService.logout(token));
    }

    @Test
    void logoutWithInvalidToken() {
        String token = "Bearer invalidToken";
        when(jwtProvider.validateToken("invalidToken")).thenReturn(false);

        assertThrows(RuntimeException.class, () -> authService.logout(token));
    }

    @Test
    void validateTokenWithValidToken() {
        String token = "Bearer validToken";
        when(jwtProvider.validateToken("validToken")).thenReturn(true);

        assertTrue(authService.validateToken(token));
    }

    @Test
    void validateTokenWithInvalidToken() {
        String token = "Bearer invalidToken";
        when(jwtProvider.validateToken("invalidToken")).thenReturn(false);

        assertFalse(authService.validateToken(token));
    }

    @Test
    void resetPasswordWithValidToken() {
        ResetPasswordRequest request = new ResetPasswordRequest("username","newPassword","1234");
        String token = "Bearer validToken";
        when(jwtProvider.validateToken("validToken")).thenReturn(true);

        ResponseEntity<?> response = authService.resetPassword(request, token);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Password reset successfully", response.getBody());
    }

    @Test
    void resetPasswordWithInvalidToken() {
        ResetPasswordRequest request = new ResetPasswordRequest("username","newPassword","1234");
        String token = "Bearer invalidToken";
        when(jwtProvider.validateToken("invalidToken")).thenReturn(false);

        ResponseEntity<?> response = authService.resetPassword(request, token);

        assertEquals(400, response.getStatusCode().value());
        assertEquals("Invalid token", response.getBody());
    }
}