package org.tinkoff.orderservice.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import io.amplicode.rautils.patch.ObjectPatcher;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.tinkoff.orderservice.client.RestaurantClient;
import org.tinkoff.orderservice.dto.CheckOrderRequestDto;
import org.tinkoff.orderservice.dto.CreateOrderRequest;
import org.tinkoff.orderservice.dto.CreateOrderResponse;
import org.tinkoff.orderservice.dto.DishDto;
import org.tinkoff.orderservice.dto.mapper.OrderMapper;
import org.tinkoff.orderservice.entity.Dish;
import org.tinkoff.orderservice.entity.Order;
import org.tinkoff.orderservice.entity.OrderDish;
import org.tinkoff.orderservice.entity.OrderDishId;
import org.tinkoff.orderservice.repository.DishRepository;
import org.tinkoff.orderservice.repository.OrderRepository;
import org.tinkoff.orderservice.service.OrderService;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {

    private final OrderMapper orderMapper;

    private final OrderRepository orderRepository;

    private final ObjectPatcher objectPatcher;

    private final RestaurantClient restaurantClient;

    private final DishRepository dishRepository;

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

    @Transactional
public ResponseEntity<?> create(CreateOrderRequest orderRequest) {
    CheckOrderRequestDto checkOrderRequestDto = new CheckOrderRequestDto();
    checkOrderRequestDto.setRestaurantId(orderRequest.getRestaurantId());
    checkOrderRequestDto.setDishQuantities(orderRequest.getDishes());

    List<DishDto> dishesListCheckResponse = restaurantClient.getFullListOfDishesIfAvailable(checkOrderRequestDto);

    if (dishesListCheckResponse.isEmpty()) {
        return ResponseEntity.badRequest().body("Some of dishes are not available");
    } else {
        Order order = orderMapper.toEntity(orderRequest);
        order.setUserId(orderRequest.getUserId());
        order.setRestaurantId(orderRequest.getRestaurantId());

        // Create and associate OrderDish entities
        List<OrderDish> orderDishes = dishesListCheckResponse.stream()
                .map(dishDto -> {
                    OrderDish orderDish = new OrderDish();
                    orderDish.setOrder(order);
                    orderDish.setDish(fetchDishById(dishDto.getId())); // Fetch Dish entity by id
                    orderDish.setQuantity(dishDto.getQuantity());

                    // Set the composite key
                    OrderDishId orderDishId = new OrderDishId();
                    orderDishId.setOrderId(order.getId());
                    orderDishId.setDishId(dishDto.getId());
                    orderDish.setId(orderDishId);

                    return orderDish;
                })
                .collect(Collectors.toList());
        order.setOrderDishes(orderDishes);

        Order resultOrder = orderRepository.save(order);
        CreateOrderResponse response = orderMapper.toResponseDto(resultOrder);

        // Map dish details
        response.setDishes(dishesListCheckResponse);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}

    public CreateOrderResponse patch(Long id, JsonNode patchNode) {
        Order order = orderRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id)));

        CreateOrderRequest createOrderRequest = orderMapper.toRequestDto(order);
        createOrderRequest = objectPatcher.patch(createOrderRequest, patchNode);
        orderMapper.updateWithNull(createOrderRequest, order);

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

    public List<CreateOrderResponse> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserId(userId).stream()
                .map(orderMapper::toResponseDto)
                .toList();
    }

    @Transactional
    @Override
    public ResponseEntity<?> setOrderStatusPreparing(Long orderId) {
        return updateOrderStatus(orderId, "preparing");
    }

    @Transactional
    @Override
    public ResponseEntity<?> setOrderStatusReady(Long orderId) {
        return updateOrderStatus(orderId, "ready");
    }

    @Transactional
    @Override
    public ResponseEntity<?> setOrderStatusDelivered(Long orderId) {
        return updateOrderStatus(orderId, "delivered");
    }

    private ResponseEntity<?> updateOrderStatus(Long orderId, String status) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order not found");
        }
        order.setStatus(status);
        orderRepository.save(order);
        return ResponseEntity.ok("Order status updated to " + status);
    }

    private Dish fetchDishById(Long dishId) {
        return dishRepository.findById(dishId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Dish with id `%s` not found".formatted(dishId)));
    }
}
