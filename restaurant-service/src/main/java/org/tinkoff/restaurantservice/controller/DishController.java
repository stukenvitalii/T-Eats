package org.tinkoff.restaurantservice.controller;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.tinkoff.restaurantservice.dto.DishDto;
import org.tinkoff.restaurantservice.service.DishService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/rest/dishes")
@RequiredArgsConstructor
public class DishController {

    private final DishService dishService;

    @GetMapping
    public List<DishDto> getAllDishes() {
        return dishService.getList();
    }

    @GetMapping("/{id}")
    public DishDto getDishById(@PathVariable("id") Long id) {
        return dishService.getOne(id);
    }

    @GetMapping("/by-ids")
    public List<DishDto> getDishesByIds(@RequestParam List<Long> ids) {
        return dishService.getMany(ids);
    }

    @PostMapping("/add")
    public DishDto createDish(@RequestBody DishDto dto) {
        return dishService.create(dto);
    }

    @PatchMapping("/{id}")
    public DishDto patchDish(@PathVariable("id") Long id, @RequestBody JsonNode patchNode) throws IOException {
        return dishService.patch(id, patchNode);
    }

    @PatchMapping
    public List<Long> patchManyDishes(@RequestParam List<Long> ids, @RequestBody JsonNode patchNode) throws IOException {
        return dishService.patchMany(ids, patchNode);
    }

    @DeleteMapping("/{id}")
    public DishDto deleteDish(@PathVariable("id") Long id) {
        return dishService.delete(id);
    }

    @DeleteMapping
    public void deleteManyDishes(@RequestParam List<Long> ids) {
        dishService.deleteMany(ids);
    }

    @GetMapping("/by-restaurant-id")
    public List<DishDto> getDishesByRestaurantId(@RequestParam Long restaurantId) {
        return dishService.getManyByRestaurantId(restaurantId);
    }
}