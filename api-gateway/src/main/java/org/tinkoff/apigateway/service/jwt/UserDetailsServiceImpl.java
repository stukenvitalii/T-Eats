package org.tinkoff.apigateway.service.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.tinkoff.apigateway.dto.UserDto;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final RestClient restClient;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ResponseEntity<UserDto> response = restClient.get()
                .uri("/rest/users/{username}", username)
                .retrieve()
                .toEntity(UserDto.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            UserDto user = response.getBody();
            return org.springframework.security.core.userdetails.User.builder()
                    .username(user.getUsername())
                    .password(user.getPassword())
                    .roles(user.getRole().getAuthority())
                    .build();
        } else {
            throw new UsernameNotFoundException("Пользователь не найден");
        }
    }
}
