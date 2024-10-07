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
    public List<DishDto> getList() {
        return dishService.getList();
    }

    @GetMapping("/{id}")
    public DishDto getOne(@PathVariable Integer id) {
        return dishService.getOne(id);
    }

    @GetMapping("/by-ids")
    public List<DishDto> getMany(@RequestParam List<Integer> ids) {
        return dishService.getMany(ids);
    }

    @PostMapping
    public DishDto create(@RequestBody DishDto dto) {
        return dishService.create(dto);
    }

    @PatchMapping("/{id}")
    public DishDto patch(@PathVariable Integer id, @RequestBody JsonNode patchNode) throws IOException {
        return dishService.patch(id, patchNode);
    }

    @PatchMapping
    public List<Integer> patchMany(@RequestParam List<Integer> ids, @RequestBody JsonNode patchNode) throws IOException {
        return dishService.patchMany(ids, patchNode);
    }

    @DeleteMapping("/{id}")
    public DishDto delete(@PathVariable Integer id) {
        return dishService.delete(id);
    }

    @DeleteMapping
    public void deleteMany(@RequestParam List<Integer> ids) {
        dishService.deleteMany(ids);
    }
}
