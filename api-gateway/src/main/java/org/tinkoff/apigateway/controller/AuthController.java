package org.tinkoff.apigateway.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tinkoff.apigateway.dto.auth.request.LoginRequest;
import org.tinkoff.apigateway.dto.auth.request.RegisterRequest;
import org.tinkoff.apigateway.dto.auth.request.ResetPasswordRequest;
import org.tinkoff.apigateway.service.jwt.AuthService;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // Регистрация нового пользователя
    @PostMapping("/register")
    @Operation(summary = "Register new user", responses = {
            @ApiResponse(description = "User registered successfully", responseCode = "200"),
            @ApiResponse(description = "Username already taken", responseCode = "400")
    })
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
        return authService.register(registerRequest);
    }

    // Аутентификация (получение JWT токена)
    @PostMapping("/login")
    @Operation(summary = "Authenticate user", responses = {
            @ApiResponse(description = "User authenticated successfully", responseCode = "200"),
            @ApiResponse(description = "Invalid credentials", responseCode = "400"),
            @ApiResponse(description = "User not found", responseCode = "400")
    })
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        return authService.authenticate(loginRequest);
    }

    // Сброс пароля
    @PostMapping("/reset-password")
    @Operation(summary = "Reset user password", responses = {
            @ApiResponse(description = "Password reset successfully", responseCode = "200")
    })
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest resetPasswordRequest,
                                           @RequestHeader("Authorization") String token) {
        return authService.resetPassword(resetPasswordRequest, token);
    }

    // Выход (аннулирование токена)
    @PostMapping("/logout")
    @Operation(summary = "Logout user", responses = {
            @ApiResponse(description = "User logged out successfully", responseCode = "200")
    })
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String token) {
        authService.logout(token);
        return ResponseEntity.ok("Logged out successfully");
    }
}
