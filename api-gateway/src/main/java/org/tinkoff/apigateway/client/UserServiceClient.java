package org.tinkoff.apigateway.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.tinkoff.apigateway.dto.UserDto;
import org.tinkoff.apigateway.dto.auth.request.RegisterRequest;
import org.tinkoff.apigateway.dto.auth.request.ResetPasswordRequest;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceClient {

    private final RestClient restClient;

    public boolean isUsernameTaken(String username) {
        log.info("Checking if username is taken: {}", username);
        return Boolean.TRUE.equals(restClient.get().uri(uriBuilder -> uriBuilder
                                .path("/rest/users/check-username")
                                .queryParam("username", username)
                                .build())
                .retrieve()
                .toEntity(Boolean.class)
                .getBody());
    }

    public void registerUser(RegisterRequest request) {
        restClient.post()
                .uri("/rest/users/add")
                .body(request)
                .retrieve()
                .toBodilessEntity();
    }

    public UserDto getUserByUsername(String username) {
        return restClient.get()
                .uri("/rest/users/by-username/{username}", username)
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
