package org.tinkoff.orderservice.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.tinkoff.orderservice.dto.CheckOrderRequestDto;
import org.tinkoff.orderservice.dto.DishDto;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class RestaurantClient {
    private final RestClient restaurantClient;

    public void getAllRestaurants() {
        restaurantClient
                .method(HttpMethod.GET)
                .uri("/rest/restaurants/")
                .retrieve()
                .toEntity(String.class);
    }

    public List<DishDto> getFullListOfDishesIfAvailable(CheckOrderRequestDto orderRequest) {
        return restaurantClient
                .method(HttpMethod.GET)
                .uri("/rest/dishes/check-availability/")
                .body(orderRequest)
                .retrieve().toEntity(new ParameterizedTypeReference<List<DishDto>>() {})
                .getBody();
    }
}
