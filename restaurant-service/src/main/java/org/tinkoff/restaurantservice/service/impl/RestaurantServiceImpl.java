package org.tinkoff.restaurantservice.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.tinkoff.restaurantservice.dto.RestaurantDto;
import org.tinkoff.restaurantservice.dto.mapper.RestaurantMapper;
import org.tinkoff.restaurantservice.entity.Restaurant;
import org.tinkoff.restaurantservice.repository.RestaurantRepository;
import org.tinkoff.restaurantservice.service.RestaurantService;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantMapper restaurantMapper;
    private final RestaurantRepository restaurantRepository;
    private final ObjectMapper objectMapper;

    public List<RestaurantDto> getList() {
        log.info("Fetching all restaurants");
        List<Restaurant> restaurants = restaurantRepository.findAll();
        return restaurants.stream()
                .map(restaurantMapper::toDto)
                .toList();
    }

    public RestaurantDto getOne(Long id) {
        log.info("Fetching restaurant with id: {}", id);
        Optional<Restaurant> restaurantOptional = restaurantRepository.findById(id);
        return restaurantMapper.toDto(restaurantOptional.orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id))));
    }

    public List<RestaurantDto> getMany(List<Long> ids) {
        log.info("Fetching restaurants with ids: {}", ids);
        List<Restaurant> restaurants = restaurantRepository.findAllById(ids);
        return restaurants.stream()
                .map(restaurantMapper::toDto)
                .toList();
    }

    public RestaurantDto create(RestaurantDto dto) {
        log.info("Creating restaurant with request: {}", dto);
        Restaurant restaurant = restaurantMapper.toEntity(dto);
        Restaurant resultRestaurant = restaurantRepository.save(restaurant);
        log.info("Restaurant created successfully with id: {}", resultRestaurant.getId());
        return restaurantMapper.toDto(resultRestaurant);
    }

    public RestaurantDto patch(Long id, JsonNode patchNode) throws IOException {
        log.info("Patching restaurant with id: {}", id);
        Restaurant restaurant = restaurantRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id)));

        RestaurantDto restaurantDto = restaurantMapper.toDto(restaurant);
        objectMapper.readerForUpdating(restaurantDto).readValue(patchNode);
        restaurantMapper.updateWithNull(restaurantDto, restaurant);

        Restaurant resultRestaurant = restaurantRepository.save(restaurant);
        log.info("Restaurant patched successfully with id: {}", resultRestaurant.getId());
        return restaurantMapper.toDto(resultRestaurant);
    }

    public List<Long> patchMany(List<Long> ids, JsonNode patchNode) throws IOException {
        log.info("Patching restaurants with ids: {}", ids);
        Collection<Restaurant> restaurants = restaurantRepository.findAllById(ids);

        for (Restaurant restaurant : restaurants) {
            RestaurantDto restaurantDto = restaurantMapper.toDto(restaurant);
            objectMapper.readerForUpdating(restaurantDto).readValue(patchNode);
            restaurantMapper.updateWithNull(restaurantDto, restaurant);
        }

        List<Restaurant> resultRestaurants = restaurantRepository.saveAll(restaurants);
        log.info("Restaurants patched successfully with ids: {}", resultRestaurants.stream().map(Restaurant::getId).toList());
        return resultRestaurants.stream().map(Restaurant::getId).toList();
    }

    public RestaurantDto delete(Long id) {
        log.info("Deleting restaurant with id: {}", id);
        Restaurant restaurant = restaurantRepository.findById(id).orElse(null);
        if (restaurant != null) {
            restaurantRepository.delete(restaurant);
            log.info("Restaurant deleted successfully with id: {}", id);
        } else {
            log.warn("Restaurant with id: {} not found", id);
        }
        return restaurantMapper.toDto(restaurant);
    }

    public void deleteMany(List<Long> ids) {
        log.info("Deleting restaurants with ids: {}", ids);
        restaurantRepository.deleteAllById(ids);
        log.info("Restaurants deleted successfully with ids: {}", ids);
    }
}