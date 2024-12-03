package org.tinkoff.restaurantservice.controller;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tinkoff.restaurantservice.dto.RestaurantDto;
import org.tinkoff.restaurantservice.service.RestaurantService;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/rest/restaurants")
public class RestaurantController {
    private final RestaurantService restaurantService;

    @GetMapping
    public List<RestaurantDto> getAllRestaurants() {
        return restaurantService.getList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestaurantDto> getRestaurantById(@PathVariable Long id) {
        return ResponseEntity.ok(restaurantService.getOne(id));
    }

    @PostMapping("/add")
    public RestaurantDto createRestaurant(@RequestBody RestaurantDto restaurant) {
        return restaurantService.create(restaurant);
    }

    @GetMapping
    public ResponseEntity<List<Long>> patchMany(@RequestParam List<Long> ids, @RequestParam JsonNode patchNodes) throws IOException {
        List<Long> longs = restaurantService.patchMany(ids, patchNodes);
        return ResponseEntity.ok(longs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestaurantDto> patch(@PathVariable Long id, @RequestParam JsonNode patchNodes) throws IOException {
        RestaurantDto restaurantDto = restaurantService.patch(id, patchNodes);
        return ResponseEntity.ok(restaurantDto);
    }
}