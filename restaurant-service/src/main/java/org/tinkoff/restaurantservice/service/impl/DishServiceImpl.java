package org.tinkoff.restaurantservice.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.tinkoff.restaurantservice.dto.CheckOrderRequestDto;
import org.tinkoff.restaurantservice.dto.DishDto;
import org.tinkoff.restaurantservice.dto.mapper.DishMapper;
import org.tinkoff.restaurantservice.entity.Dish;
import org.tinkoff.restaurantservice.repository.DishRepository;
import org.tinkoff.restaurantservice.service.DishService;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class DishServiceImpl implements DishService {

    private final DishMapper dishMapper;

    private final DishRepository dishRepository;

    private final ObjectMapper objectMapper;

    public List<DishDto> getList() {
        List<Dish> dishes = dishRepository.findAll();
        return dishes.stream()
                .map(dishMapper::toDto)
                .toList();
    }

    public DishDto getOne(Long id) {
        Optional<Dish> dishOptional = dishRepository.findById(id);
        return dishMapper.toDto(dishOptional.orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id))));
    }

    public List<DishDto> getMany(List<Long> ids) {
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

    public DishDto patch(Long id, JsonNode patchNode) throws IOException {
        Dish dish = dishRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id)));

        DishDto dishDto = dishMapper.toDto(dish);
        objectMapper.readerForUpdating(dishDto).readValue(patchNode);
        dishMapper.updateWithNull(dishDto, dish);

        Dish resultDish = dishRepository.save(dish);
        return dishMapper.toDto(resultDish);
    }

    public List<Long> patchMany(List<Long> ids, JsonNode patchNode) throws IOException {
        Collection<Dish> dishes = dishRepository.findAllById(ids);

        for (Dish dish : dishes) {
            DishDto dishDto = dishMapper.toDto(dish);
            objectMapper.readerForUpdating(dishDto).readValue(patchNode);
            dishMapper.updateWithNull(dishDto, dish);
        }

        List<Dish> resultDishes = dishRepository.saveAll(dishes);
        return resultDishes.stream().map(Dish::getId).toList();
    }

    public DishDto delete(Long id) {
        Dish dish = dishRepository.findById(id).orElse(null);
        if (dish != null) {
            dishRepository.delete(dish);
        }
        return dishMapper.toDto(dish);
    }

    public void deleteMany(List<Long> ids) {
        dishRepository.deleteAllById(ids);
    }

    public List<DishDto> getManyByRestaurantId(Long id) {
        List<Dish> dishes = dishRepository.findAllByRestaurantId(id);
        return dishes.stream()
                .map(dishMapper::toDto)
                .toList();
    }

    public ResponseEntity<List<DishDto>> returnListIfDishesIfAllAreAvailable(CheckOrderRequestDto orderRequest) {
    Long restaurantId = orderRequest.getRestaurantId();
    Map<Long, Integer> dishQuantities = orderRequest.getDishQuantities();

    // Check availability of all dishes
    for (Map.Entry<Long, Integer> entry : dishQuantities.entrySet()) {
        Long dishId = entry.getKey();
        Integer requiredQuantity = entry.getValue();

        Dish dish = dishRepository.findByIdAndRestaurantId(dishId, restaurantId);
        if (dish == null || dish.getQuantity() < requiredQuantity) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(List.of());
        }
    }

    // If all dishes are available, return the list of DishDto
    List<DishDto> availableDishes = dishQuantities.keySet().stream()
            .map(dishId -> {
                Dish dish = dishRepository.findByIdAndRestaurantId(dishId, restaurantId);
                return dishMapper.toDto(dish);
            })
            .toList();

    // Reduce the quantities after returning the list
    dishQuantities.forEach((dishId, requiredQuantity) -> {
        Dish dish = dishRepository.findByIdAndRestaurantId(dishId, restaurantId);
        dish.setQuantity(dish.getQuantity() - requiredQuantity);
        dishRepository.save(dish);
    });

    return ResponseEntity.ok(availableDishes);
}
}
