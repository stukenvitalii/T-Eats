package org.tinkoff.orderservice.controller;
import com.fasterxml.jackson.databind.JsonNode;
import io.amplicode.rautils.patch.ObjectPatcher;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.tinkoff.orderservice.dto.OrderDto;
import org.tinkoff.orderservice.dto.mapper.OrderMapper;
import org.tinkoff.orderservice.entity.Order;
import org.tinkoff.orderservice.repository.OrderRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/rest/orders")
@RequiredArgsConstructor
public class OrderResource {

    private final OrderRepository orderRepository;

    private final OrderMapper orderMapper;

    private final ObjectPatcher objectPatcher;

    @GetMapping
    public List<OrderDto> getList() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(orderMapper::toDto)
                .toList();
    }

    @GetMapping("/{id}")
    public OrderDto getOne(@PathVariable Integer id) {
        Optional<Order> orderOptional = orderRepository.findById(id);
        return orderMapper.toDto(orderOptional.orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id))));
    }

    @GetMapping("/by-ids")
    public List<OrderDto> getMany(@RequestParam List<Integer> ids) {
        List<Order> orders = orderRepository.findAllById(ids);
        return orders.stream()
                .map(orderMapper::toDto)
                .toList();
    }

    @PostMapping
    public OrderDto create(@RequestBody OrderDto dto) {
        Order order = orderMapper.toEntity(dto);
        Order resultOrder = orderRepository.save(order);
        return orderMapper.toDto(resultOrder);
    }

    @PatchMapping("/{id}")
    public OrderDto patch(@PathVariable Integer id, @RequestBody JsonNode patchNode) {
        Order order = orderRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id)));

        OrderDto orderDto = orderMapper.toDto(order);
        orderDto = objectPatcher.patch(orderDto, patchNode);
        orderMapper.updateWithNull(orderDto, order);

        Order resultOrder = orderRepository.save(order);
        return orderMapper.toDto(resultOrder);
    }

    @PatchMapping
    public List<Integer> patchMany(@RequestParam List<Integer> ids, @RequestBody JsonNode patchNode) {
        Collection<Order> orders = orderRepository.findAllById(ids);

        for (Order order : orders) {
            OrderDto orderDto = orderMapper.toDto(order);
            orderDto = objectPatcher.patch(orderDto, patchNode);
            orderMapper.updateWithNull(orderDto, order);
        }

        List<Order> resultOrders = orderRepository.saveAll(orders);
        return resultOrders.stream().map(Order::getId).toList();
    }

    @DeleteMapping("/{id}")
    public OrderDto delete(@PathVariable Integer id) {
        Order order = orderRepository.findById(id).orElse(null);
        if (order != null) {
            orderRepository.delete(order);
        }
        return orderMapper.toDto(order);
    }

    @DeleteMapping
    public void deleteMany(@RequestParam List<Integer> ids) {
        orderRepository.deleteAllById(ids);
    }
}
