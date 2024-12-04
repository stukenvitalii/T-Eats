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

    public String authenticate(LoginRequest loginRequest) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();
        boolean rememberMe = loginRequest.isRememberMe();

        // Аутентификация пользователя
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        UserDto user = userServiceClient.getUserByUsername(username);
        if (user == null) {
            throw new RuntimeException("User not found");
        }

//        Token token = new Token();
//        token.setToken(jwt);
//        token.setExpiryDate(rememberMe ?
//                Instant.now().plus(jwtProvider.getJwtExpirationDays(), ChronoUnit.DAYS) :
//                Instant.now().plus(jwtProvider.getJwtExpirationMinutes(), ChronoUnit.MINUTES)
//        );
//        token.setUser(user);
//        token.setActive(true);
//
//        tokenRepository.save(token);

        log.info("User authenticated: {}", userDetails.getUsername());

        // Генерация JWT токена
        return jwtProvider.generateToken(userDetails.getUsername(), rememberMe);
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
}
