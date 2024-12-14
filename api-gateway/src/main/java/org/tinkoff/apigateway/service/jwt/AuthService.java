package org.tinkoff.apigateway.service.jwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.tinkoff.apigateway.client.UserServiceClient;
import org.tinkoff.apigateway.dto.UserDto;
import org.tinkoff.apigateway.dto.auth.request.LoginRequest;
import org.tinkoff.apigateway.dto.auth.request.RegisterRequest;
import org.tinkoff.apigateway.dto.auth.request.ResetPasswordRequest;
import org.tinkoff.apigateway.dto.auth.response.JwtResponse;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtProvider jwtProvider;
    private final AuthenticationManager authenticationManager;
    private final UserServiceClient userServiceClient;
    private final PasswordEncoder passwordEncoder;

    public ResponseEntity<?> register(RegisterRequest registerRequest) {
        // Проверка доступности имени пользователя через UserService
        if (userServiceClient.isUsernameTaken(registerRequest.getUsername())) {
            log.info("Username is already taken: {}", registerRequest.getUsername());
            return ResponseEntity.badRequest().body("Username already taken");
        }

        log.info("Registering new user: {}", registerRequest.getUsername());

        // Хеширование пароля
        registerRequest.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        // Создание нового пользователя в UserService
        userServiceClient.registerUser(registerRequest);

        // Генерация JWT токена для нового пользователя
        String token = jwtProvider.generateToken(registerRequest.getUsername(), false);
        log.info("New user registered: {}", registerRequest.getUsername());
        return ResponseEntity.ok(new JwtResponse(token));
    }

    public ResponseEntity<?> authenticate(LoginRequest loginRequest) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();
        boolean rememberMe = loginRequest.isRememberMe();

        UserDto user = userServiceClient.getUserByUsername(username);
        if (user == null) {
            return ResponseEntity.badRequest().body("User not found");
        } else if (!areCredentialsValid(user, username, password)) {
            return ResponseEntity.badRequest().body("Invalid credentials");
        }

        log.info("User authenticated: {}", user.getUsername());

        // Generate JWT token
        String token = jwtProvider.generateToken(user.getUsername(), rememberMe);
        return ResponseEntity.ok(new JwtResponse(token));
}

    public void logout(String token) {
        token = token.replace("Bearer ", "");
        if (!jwtProvider.validateToken(token)) {
            throw new RuntimeException("Invalid token");
        }
    }

    public boolean validateToken(String token) {
        token = token.replace("Bearer ", "");
        // Локальная валидация токена через JwtProvider
        return jwtProvider.validateToken(token);
    }

    public ResponseEntity<?> resetPassword(ResetPasswordRequest request, String token) {
        token = token.replace("Bearer ", "");
        if (!jwtProvider.validateToken(token)) {
            return ResponseEntity.badRequest().body("Invalid token");
        }

        // Сброс пароля через UserService
        userServiceClient.resetPassword(request);

        return ResponseEntity.ok("Password reset successfully");
    }

    private boolean areCredentialsValid(UserDto userFromDb, String username, String password) {
        return userFromDb != null &&
                userFromDb.getUsername().equals(username) &&
                passwordEncoder.matches(password, userFromDb.getPassword());
    }
}
