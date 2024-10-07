package org.tinkoff.restaurantservice.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.tinkoff.restaurantservice.dto.DishDto;
import org.tinkoff.restaurantservice.dto.mapper.DishMapper;
import org.tinkoff.restaurantservice.entity.Dish;
import org.tinkoff.restaurantservice.entity.Restaurant;
import org.tinkoff.restaurantservice.repository.DishRepository;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class DishService{

    private final DishMapper dishMapper;

    private final DishRepository dishRepository;

    private final ObjectMapper objectMapper;

    public List<DishDto> getList() {
        List<Dish> dishes = dishRepository.findAll();
        return dishes.stream()
                .map(dishMapper::toDto)
                .toList();
    }

    public DishDto getOne(Integer id) {
        Optional<Dish> dishOptional = dishRepository.findById(id);
        return dishMapper.toDto(dishOptional.orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id))));
    }

    public List<DishDto> getMany(List<Integer> ids) {
        List<Dish> dishes = dishRepository.findAllById(ids);
        return dishes.stream()
                .map(dishMapper::toDto)
                .toList();
    }

    public DishDto create(DishDto dto) {
        Dish dish = dishMapper.toEntity(dto);
        Dish resultDish = dishRepository.save(dish);
        return dishMapper.toDto(resultDish);
    }

    public DishDto patch(Integer id, JsonNode patchNode) throws IOException {
        Dish dish = dishRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id)));

        DishDto dishDto = dishMapper.toDto(dish);
        objectMapper.readerForUpdating(dishDto).readValue(patchNode);
        dishMapper.updateWithNull(dishDto, dish);

        Dish resultDish = dishRepository.save(dish);
        return dishMapper.toDto(resultDish);
    }

    public List<Integer> patchMany(List<Integer> ids, JsonNode patchNode) throws IOException {
        Collection<Dish> dishes = dishRepository.findAllById(ids);

        for (Dish dish : dishes) {
            DishDto dishDto = dishMapper.toDto(dish);
            objectMapper.readerForUpdating(dishDto).readValue(patchNode);
            dishMapper.updateWithNull(dishDto, dish);
        }

        List<Dish> resultDishes = dishRepository.saveAll(dishes);
        return resultDishes.stream().map(Dish::getId).toList();
    }

    public DishDto delete(Integer id) {
        Dish dish = dishRepository.findById(id).orElse(null);
        if (dish != null) {
            dishRepository.delete(dish);
        }
        return dishMapper.toDto(dish);
    }

    public void deleteMany(List<Integer> ids) {
        dishRepository.deleteAllById(ids);
    }

    public List<DishDto> findAllByRestaurant_Id(Integer id) {
        List<Dish> dishes = dishRepository.findAllByRestaurant_Id(id);
        return dishes.stream()
                .map(dishMapper::toDto)
                .toList();
    }
}
