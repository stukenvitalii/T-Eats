package org.tinkoff.orderservice.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
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

    public List<DishDto> getFullListOfDishesIfAvailable(CheckOrderRequestDto orderRequest) {
        return restaurantClient
                .method(HttpMethod.POST)
                .uri("/rest/dishes/check-availability")
                .contentType(MediaType.APPLICATION_JSON)
                .body(orderRequest)
                .retrieve()
                .onStatus(HttpStatusCode::is5xxServerError, (request, response)  -> {
                    log.error("Internal error during request to RESTAURANT-SERVICE with method {}, to URI {}", request.getMethod(), request.getURI());
                })
                .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                    log.error("Client error during request to RESTAURANT-SERVICE with method {}, to URI {}", request.getMethod(), request.getURI());
                })
                .toEntity(new ParameterizedTypeReference<List<DishDto>>() {})
                .getBody();
    }
}
