package org.tinkoff.orderservice.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
@Slf4j
public class RestaurantClient {
    private final RestClient restaurantClient;

    public void getAllRestaurants() {
        restaurantClient
                .method(HttpMethod.GET)
                .uri("/restaurants/")
                .retrieve()
                .toEntity(String.class);
    }
}
