package org.tinkoff.orderservice.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.tinkoff.orderservice.dto.CreateOrderRequest;
import org.tinkoff.orderservice.dto.CreateOrderResponse;

import java.util.List;

public interface OrderService {
    List<CreateOrderResponse> getList();

    CreateOrderResponse getOne(Long id);

    List<CreateOrderResponse> getMany(List<Long> ids);

    CreateOrderResponse create(CreateOrderRequest orderRequest);

    CreateOrderResponse patch(Long id, JsonNode patchNode);

    List<Long> patchMany(List<Long> ids, JsonNode patchNode);

    Long delete(Long id);

    void deleteMany(List<Long> ids);

    List<CreateOrderResponse> getOrdersByUserId(Long userId);
}
