package org.tinkoff.orderservice.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.amplicode.rautils.patch.ObjectPatcher;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.tinkoff.orderservice.dto.mapper.OrderMapper;
import org.tinkoff.orderservice.dto.OrderDto;
import org.tinkoff.orderservice.entity.Order;
import org.tinkoff.orderservice.repository.OrderRepository;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class OrderService{

    private final OrderMapper orderMapper;

    private final OrderRepository orderRepository;

    private final ObjectMapper objectMapper;

    private final ObjectPatcher objectPatcher;

    public List<OrderDto> getList() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(orderMapper::toDto)
                .toList();
    }

    public OrderDto getOne(Integer id) {
        Optional<Order> orderOptional = orderRepository.findById(id);
        return orderMapper.toDto(orderOptional.orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id))));
    }

    public List<OrderDto> getMany(List<Integer> ids) {
        List<Order> orders = orderRepository.findAllById(ids);
        return orders.stream()
                .map(orderMapper::toDto)
                .toList();
    }

    public OrderDto create(OrderDto dto) {
        Order order = orderMapper.toEntity(dto);
        Order resultOrder = orderRepository.save(order);
        return orderMapper.toDto(resultOrder);
    }

    public OrderDto patch(Integer id, JsonNode patchNode) throws IOException {
        Order order = orderRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id)));

        OrderDto orderDto = orderMapper.toDto(order);
        objectMapper.readerForUpdating(orderDto).readValue(patchNode);
        orderMapper.updateWithNull(orderDto, order);

        Order resultOrder = orderRepository.save(order);
        return orderMapper.toDto(resultOrder);
    }

    public List<Integer> patchMany(List<Integer> ids, JsonNode patchNode) throws IOException {
        Collection<Order> orders = orderRepository.findAllById(ids);

        for (Order order : orders) {
            OrderDto orderDto = orderMapper.toDto(order);
            objectMapper.readerForUpdating(orderDto).readValue(patchNode);
            orderMapper.updateWithNull(orderDto, order);
        }

        List<Order> resultOrders = orderRepository.saveAll(orders);

        return resultOrders.stream().map(Order::getId).toList();
    }

    public OrderDto delete(Integer id) {
        Order order = orderRepository.findById(id).orElse(null);
        if (order != null) {
            orderRepository.delete(order);
        }
        return orderMapper.toDto(order);
    }

    public void deleteMany(List<Integer> ids) {
        orderRepository.deleteAllById(ids);
    }
}
