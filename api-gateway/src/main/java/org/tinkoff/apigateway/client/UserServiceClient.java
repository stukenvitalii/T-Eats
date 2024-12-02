package org.tinkoff.apigateway.client;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.tinkoff.apigateway.dto.UserDto;
import org.tinkoff.apigateway.dto.auth.request.RegisterRequest;
import org.tinkoff.apigateway.dto.auth.request.ResetPasswordRequest;

@Service
@RequiredArgsConstructor
public class UserServiceClient {

    private final RestClient restClient;

    public boolean isUsernameTaken(String username) {
        return restClient.get()
                .uri("/users/check-username?username=", username)
                .retrieve()
                .toEntity(Boolean.class)
                .getBody();
    }

    public void registerUser(RegisterRequest request) {
        restClient.post()
                .uri("/users/register")
                .body(request)
                .retrieve()
                .toBodilessEntity();
    }

    public UserDto getUserByUsername(String username) {
        return restClient.get()
                .uri("/users/{username}", username)
                .retrieve()
                .toEntity(UserDto.class)
                .getBody();
    }

    public void resetPassword(ResetPasswordRequest request) {
        restClient.post()
                .uri("/users/reset-password")
                .body(request)
                .retrieve()
                .toBodilessEntity();
    }
}
