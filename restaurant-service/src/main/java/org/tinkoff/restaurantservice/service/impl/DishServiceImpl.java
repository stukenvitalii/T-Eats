package org.tinkoff.restaurantservice.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class DishServiceImpl implements DishService {

    private final DishMapper dishMapper;
    private final DishRepository dishRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    public List<DishDto> getList() {
        log.info("Fetching all dishes");
        List<Dish> dishes = dishRepository.findAll();
        return dishes.stream()
                .map(dishMapper::toDto)
                .toList();
    }

    @Transactional
    public DishDto getOne(Long id) {
        log.info("Fetching dish with id: {}", id);
        Optional<Dish> dishOptional = dishRepository.findById(id);
        return dishMapper.toDto(dishOptional.orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id))));
    }

    @Transactional
    public List<DishDto> getMany(List<Long> ids) {
        log.info("Fetching dishes with ids: {}", ids);
        List<Dish> dishes = dishRepository.findAllById(ids);
        return dishes.stream()
                .map(dishMapper::toDto)
                .toList();
    }

    @Transactional
    public DishDto create(DishDto dto) {
        log.info("Creating dish with request: {}", dto);
        Dish dish = dishMapper.toEntity(dto);
        Dish resultDish = dishRepository.save(dish);
        log.info("Dish created successfully with id: {}", resultDish.getId());
        return dishMapper.toDto(resultDish);
    }

    @Transactional
    public DishDto patch(Long id, JsonNode patchNode) throws IOException {
        log.info("Patching dish with id: {}", id);
        Dish dish = dishRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id)));

        DishDto dishDto = dishMapper.toDto(dish);
        objectMapper.readerForUpdating(dishDto).readValue(patchNode);
        dishMapper.updateWithNull(dishDto, dish);

        Dish resultDish = dishRepository.save(dish);
        log.info("Dish patched successfully with id: {}", resultDish.getId());
        return dishMapper.toDto(resultDish);
    }

    @Transactional
    public List<Long> patchMany(List<Long> ids, JsonNode patchNode) throws IOException {
        log.info("Patching dishes with ids: {}", ids);
        Collection<Dish> dishes = dishRepository.findAllById(ids);

        for (Dish dish : dishes) {
            DishDto dishDto = dishMapper.toDto(dish);
            objectMapper.readerForUpdating(dishDto).readValue(patchNode);
            dishMapper.updateWithNull(dishDto, dish);
        }

        List<Dish> resultDishes = dishRepository.saveAll(dishes);
        log.info("Dishes patched successfully with ids: {}", resultDishes.stream().map(Dish::getId).toList());
        return resultDishes.stream().map(Dish::getId).toList();
    }

    @Transactional
    public DishDto delete(Long id) {
        log.info("Deleting dish with id: {}", id);
        Dish dish = dishRepository.findById(id).orElse(null);
        if (dish != null) {
            dishRepository.delete(dish);
            log.info("Dish deleted successfully with id: {}", id);
        } else {
            log.warn("Dish with id: {} not found", id);
        }
        return dishMapper.toDto(dish);
    }

    @Transactional
    public void deleteMany(List<Long> ids) {
        log.info("Deleting dishes with ids: {}", ids);
        dishRepository.deleteAllById(ids);
        log.info("Dishes deleted successfully with ids: {}", ids);
    }

    @Transactional
    public List<DishDto> getManyByRestaurantId(Long id) {
        log.info("Fetching dishes for restaurant with id: {}", id);
        List<Dish> dishes = dishRepository.findAllByRestaurantId(id);
        return dishes.stream()
                .map(dishMapper::toDto)
                .toList();
    }

    @Transactional
    public ResponseEntity<List<DishDto>> returnListIfDishesIfAllAreAvailable(CheckOrderRequestDto orderRequest) {
        log.info("Checking availability of dishes for order request: {}", orderRequest);
        Long restaurantId = orderRequest.getRestaurantId();
        Map<Long, Integer> dishQuantities = orderRequest.getDishQuantities();

        // Fetch all dishes by restaurant ID
        List<Dish> dishes = dishRepository.findAllByRestaurantId(restaurantId);
        Map<Long, Dish> dishMap = dishes.stream().collect(Collectors.toMap(Dish::getId, dish -> dish));

        // Check availability of all dishes
        for (Map.Entry<Long, Integer> entry : dishQuantities.entrySet()) {
            Long dishId = entry.getKey();
            Integer requiredQuantity = entry.getValue();

            Dish dish = dishMap.get(dishId);
            if (dish == null || dish.getQuantity() < requiredQuantity) {
                log.warn("Dish with id: {} is not available in required quantity: {}", dishId, requiredQuantity);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(List.of());
            }
        }

        // If all dishes are available, return the list of DishDto
        List<DishDto> availableDishes = dishQuantities.keySet().stream()
                .map(dishId -> {
                    Dish dish = dishMap.get(dishId);
                    DishDto dishDto = dishMapper.toDto(dish);
                    dishDto.setQuantity(dishQuantities.get(dishId));
                    return dishDto;
                })
                .toList();

        // Reduce the quantities after returning the list
        dishQuantities.forEach((dishId, requiredQuantity) -> {
            Dish dish = dishMap.get(dishId);
            dish.setQuantity(dish.getQuantity() - requiredQuantity);
            dishRepository.save(dish);
        });

        log.info("All requested dishes are available and quantities updated");
        return ResponseEntity.ok(availableDishes);
    }
}