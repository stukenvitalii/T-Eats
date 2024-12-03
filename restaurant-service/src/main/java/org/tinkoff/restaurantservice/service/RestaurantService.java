package org.tinkoff.restaurantservice.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.tinkoff.restaurantservice.dto.RestaurantDto;
import org.tinkoff.restaurantservice.dto.mapper.RestaurantMapper;
import org.tinkoff.restaurantservice.entity.Restaurant;
import org.tinkoff.restaurantservice.repository.RestaurantRepository;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class RestaurantService {

    private final RestaurantMapper restaurantMapper;

    private final RestaurantRepository restaurantRepository;

    private final ObjectMapper objectMapper;

    public List<RestaurantDto> getList() {
        List<Restaurant> restaurants = restaurantRepository.findAll();
        return restaurants.stream()
                .map(restaurantMapper::toDto)
                .toList();
    }

    public RestaurantDto getOne(Integer id) {
        Optional<Restaurant> restaurantOptional = restaurantRepository.findById(id);
        return restaurantMapper.toDto(restaurantOptional.orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id))));
    }

    public List<RestaurantDto> getMany(List<Integer> ids) {
        List<Restaurant> restaurants = restaurantRepository.findAllById(ids);
        return restaurants.stream()
                .map(restaurantMapper::toDto)
                .toList();
    }

    public RestaurantDto create(RestaurantDto dto) {
        Restaurant restaurant = restaurantMapper.toEntity(dto);
        Restaurant resultRestaurant = restaurantRepository.save(restaurant);
        return restaurantMapper.toDto(resultRestaurant);
    }

    public RestaurantDto patch(Integer id, JsonNode patchNode) throws IOException {
        Restaurant restaurant = restaurantRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id)));

        RestaurantDto restaurantDto = restaurantMapper.toDto(restaurant);
        objectMapper.readerForUpdating(restaurantDto).readValue(patchNode);
        restaurantMapper.updateWithNull(restaurantDto, restaurant);

        Restaurant resultRestaurant = restaurantRepository.save(restaurant);
        return restaurantMapper.toDto(resultRestaurant);
    }

    public List<Integer> patchMany(List<Integer> ids, JsonNode patchNode) throws IOException {
        Collection<Restaurant> restaurants = restaurantRepository.findAllById(ids);

        for (Restaurant restaurant : restaurants) {
            RestaurantDto restaurantDto = restaurantMapper.toDto(restaurant);
            objectMapper.readerForUpdating(restaurantDto).readValue(patchNode);
            restaurantMapper.updateWithNull(restaurantDto, restaurant);
        }

        List<Restaurant> resultRestaurants = restaurantRepository.saveAll(restaurants);

        return resultRestaurants.stream().map(Restaurant::getId).toList();
    }

    public RestaurantDto delete(Integer id) {
        Restaurant restaurant = restaurantRepository.findById(id).orElse(null);
        if (restaurant != null) {
            restaurantRepository.delete(restaurant);
        }
        return restaurantMapper.toDto(restaurant);
    }

    public void deleteMany(List<Integer> ids) {
        restaurantRepository.deleteAllById(ids);
    }
}
