package org.tinkoff.restaurantservice.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.tinkoff.restaurantservice.dto.RestaurantDto;

import java.io.IOException;
import java.util.List;

public interface RestaurantService {
    List<RestaurantDto> getList();

    RestaurantDto getOne(Long id);

    List<RestaurantDto> getMany(List<Long> ids);

    RestaurantDto create(RestaurantDto dto);

    RestaurantDto patch(Long id, JsonNode patchNode) throws IOException;

    List<Long> patchMany(List<Long> ids, JsonNode patchNode) throws IOException;

    RestaurantDto delete(Long id);

    void deleteMany(List<Long> ids);
}
