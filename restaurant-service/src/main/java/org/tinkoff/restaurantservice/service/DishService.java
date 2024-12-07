package org.tinkoff.restaurantservice.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.ResponseEntity;
import org.tinkoff.restaurantservice.dto.CheckOrderRequestDto;
import org.tinkoff.restaurantservice.dto.DishDto;

import java.io.IOException;
import java.util.List;

public interface DishService {
    List<DishDto> getList();

    DishDto getOne(Long id);

    List<DishDto> getMany(List<Long> ids);

    DishDto create(DishDto dto);

    DishDto patch(Long id, JsonNode patchNode) throws IOException;

    List<Long> patchMany(List<Long> ids, JsonNode patchNode) throws IOException;

    DishDto delete(Long id);

    void deleteMany(List<Long> ids);

    List<DishDto> getManyByRestaurantId(Long restaurantId);

    ResponseEntity<List<DishDto>> returnListIfDishesIfAllAreAvailable(CheckOrderRequestDto orderRequest);
}
