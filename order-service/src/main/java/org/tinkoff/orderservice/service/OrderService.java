package org.tinkoff.orderservice.service;

import com.fasterxml.jackson.databind.JsonNode;
import io.amplicode.rautils.patch.ObjectPatcher;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.tinkoff.orderservice.dto.CreateOrderRequest;
import org.tinkoff.orderservice.dto.CreateOrderResponse;
import org.tinkoff.orderservice.dto.mapper.OrderMapper;
import org.tinkoff.orderservice.entity.Order;
import org.tinkoff.orderservice.repository.OrderRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class OrderService {

    private final OrderMapper orderMapper;

    private final OrderRepository orderRepository;

    private final ObjectPatcher objectPatcher;

    public List<CreateOrderResponse> getList() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(orderMapper::toResponseDto)
                .toList();
    }

    public CreateOrderResponse getOne(Long id) {
        Optional<Order> orderOptional = orderRepository.findById(id);
        return orderMapper.toResponseDto(orderOptional.orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id))));
    }

    public List<CreateOrderResponse> getMany(List<Long> ids) {
        List<Order> orders = orderRepository.findAllById(ids);
        return orders.stream()
                .map(orderMapper::toResponseDto)
                .toList();
    }

    public CreateOrderResponse create(CreateOrderRequest orderRequest) {
        Order order = orderMapper.toEntity(orderRequest);
        order.setUserId(orderRequest.getUserId());
        order.setRestaurantId(orderRequest.getRestaurantId());

        Order resultOrder = orderRepository.save(order);
        return orderMapper.toResponseDto(resultOrder);
    }

    public CreateOrderResponse patch(Long id, JsonNode patchNode) {
        Order order = orderRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id)));

        CreateOrderRequest createOrderRequest = orderMapper.toRequestDto(order);
        createOrderRequest = objectPatcher.patch(createOrderRequest, patchNode);
        orderMapper.updateWithNull(createOrderRequest, order); //TODO maybe error here

        Order resultOrder = orderRepository.save(order);
        return orderMapper.toResponseDto(resultOrder);
    }

    public List<Long> patchMany(List<Long> ids, JsonNode patchNode) {
        Collection<Order> orders = orderRepository.findAllById(ids);

        for (Order order : orders) {
            CreateOrderRequest createOrderRequest = orderMapper.toRequestDto(order);
            createOrderRequest = objectPatcher.patch(createOrderRequest, patchNode);
            orderMapper.updateWithNull(createOrderRequest, order);
        }

        List<Order> resultOrders = orderRepository.saveAll(orders);
        return resultOrders.stream()
                .map(Order::getId)
                .toList();
    }

    public Long delete(Long id) {
        Order order = orderRepository.findById(id).orElse(null);
        if (order != null) {
            orderRepository.delete(order);
            return id;
        }
        return null;
    }

    public void deleteMany(List<Long> ids) {
        orderRepository.deleteAllById(ids);
    }

    public List<Order> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserId(userId);
    }
}
