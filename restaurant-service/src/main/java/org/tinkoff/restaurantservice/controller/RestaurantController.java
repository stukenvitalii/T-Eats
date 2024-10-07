package org.tinkoff.restaurantservice.controller;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.tinkoff.restaurantservice.dto.DishDto;
import org.tinkoff.restaurantservice.dto.RestaurantDto;
import org.tinkoff.restaurantservice.service.DishService;
import org.tinkoff.restaurantservice.service.RestaurantService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/rest/restaurants")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;
    private final DishService dishService;

    @GetMapping
    public List<RestaurantDto> getList() {
        return restaurantService.getList();
    }

    @GetMapping("/{id}")
    public RestaurantDto getOne(@PathVariable Integer id) {
        return restaurantService.getOne(id);
    }

    @GetMapping("/by-ids")
    public List<RestaurantDto> getMany(@RequestParam List<Integer> ids) {
        return restaurantService.getMany(ids);
    }

    @PostMapping
    public RestaurantDto create(@RequestBody RestaurantDto dto) {
        return restaurantService.create(dto);
    }

    @PatchMapping("/{id}")
    public RestaurantDto patch(@PathVariable Integer id, @RequestBody JsonNode patchNode) throws IOException {
        return restaurantService.patch(id, patchNode);
    }

    @PatchMapping
    public List<Integer> patchMany(@RequestParam List<Integer> ids, @RequestBody JsonNode patchNode) throws IOException {
        return restaurantService.patchMany(ids, patchNode);
    }

    @DeleteMapping("/{id}")
    public RestaurantDto delete(@PathVariable Integer id) {
        return restaurantService.delete(id);
    }

    @DeleteMapping
    public void deleteMany(@RequestParam List<Integer> ids) {
        restaurantService.deleteMany(ids);
    }

    @GetMapping("/dishes/{id}")
    public List<DishDto> getDishes(@PathVariable Integer id) {
        return dishService.findAllByRestaurant_Id(id);
    }
}
