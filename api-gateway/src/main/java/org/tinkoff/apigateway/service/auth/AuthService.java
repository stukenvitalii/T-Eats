package org.tinkoff.apigateway.service.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.tinkoff.apigateway.client.UserServiceClient;
import org.tinkoff.apigateway.dto.UserDto;
import org.tinkoff.apigateway.dto.auth.request.LoginRequest;
import org.tinkoff.apigateway.dto.auth.request.RegisterRequest;
import org.tinkoff.apigateway.dto.auth.request.ResetPasswordRequest;
import org.tinkoff.apigateway.dto.auth.response.JwtResponse;
import org.tinkoff.apigateway.service.jwt.JwtProvider;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtProvider jwtProvider;
    private final UserServiceClient userServiceClient;
    private final PasswordEncoder passwordEncoder;

    public ResponseEntity<?> register(RegisterRequest registerRequest) {
        log.info("Attempting to register user: {}", registerRequest.getUsername());
        if (userServiceClient.isUsernameTaken(registerRequest.getUsername())) {
            log.warn("Username is already taken: {}", registerRequest.getUsername());
            return ResponseEntity.badRequest().body("Username already taken");
        }

        log.info("Registering new user: {}", registerRequest.getUsername());
        registerRequest.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        userServiceClient.registerUser(registerRequest);

        String token = jwtProvider.generateToken(registerRequest.getUsername(), false);
        log.info("User registered successfully: {}", registerRequest.getUsername());
        return ResponseEntity.ok(new JwtResponse(token));
    }

    public ResponseEntity<?> authenticate(LoginRequest loginRequest) {
        log.info("Attempting to authenticate user: {}", loginRequest.getUsername());
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();
        boolean rememberMe = loginRequest.isRememberMe();

        UserDto user = userServiceClient.getUserByUsername(username);
        if (user == null) {
            log.warn("User not found: {}", username);
            return ResponseEntity.badRequest().body("User not found");
        } else if (!areCredentialsValid(user, username, password)) {
            log.warn("Invalid credentials for user: {}", username);
            return ResponseEntity.badRequest().body("Invalid credentials");
        }

        log.info("User authenticated successfully: {}", username);
        String token = jwtProvider.generateToken(user.getUsername(), rememberMe);
        return ResponseEntity.ok(new JwtResponse(token));
    }

    public void logout(String token) {
        log.info("Attempting to logout with token: {}", token);
        token = token.replace("Bearer ", "");
        if (!jwtProvider.validateToken(token)) {
            log.error("Invalid token during logout: {}", token);
            throw new RuntimeException("Invalid token");
        }
        log.info("Logout successful");
    }

    public boolean validateToken(String token) {
        log.info("Validating token: {}", token);
        token = token.replace("Bearer ", "");
        boolean isValid = jwtProvider.validateToken(token);
        log.info("Token validation result: {}", isValid);
        return isValid;
    }

    public ResponseEntity<?> resetPassword(ResetPasswordRequest request, String token) {
        log.info("Attempting to reset password with token: {}", token);
        token = token.replace("Bearer ", "");
        if (!jwtProvider.validateToken(token)) {
            log.error("Invalid token during password reset: {}", token);
            return ResponseEntity.badRequest().body("Invalid token");
        }

        userServiceClient.resetPassword(request);
        log.info("Password reset successfully for user: {}", request.getUsername());
        return ResponseEntity.ok("Password reset successfully");
    }

    private boolean areCredentialsValid(UserDto userFromDb, String username, String password) {
        log.info("Validating credentials for user: {}", username);
        boolean isValid = userFromDb != null &&
                userFromDb.getUsername().equals(username) &&
                passwordEncoder.matches(password, userFromDb.getPassword());
        log.info("Credentials validation result for user {}: {}", username, isValid);
        return isValid;
    }
}