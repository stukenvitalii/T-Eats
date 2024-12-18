package org.tinkoff.orderservice.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import io.amplicode.rautils.patch.ObjectPatcher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {

    private final OrderMapper orderMapper;
    private final OrderRepository orderRepository;
    private final ObjectPatcher objectPatcher;
    private final RestaurantClient restaurantClient;
    private final DishRepository dishRepository;

    @Transactional
    public List<CreateOrderResponse> getList() {
        log.info("Fetching all orders");
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(orderMapper::toResponseDto)
                .toList();
    }

    @Transactional
    public CreateOrderResponse getOne(Long id) {
        log.info("Fetching order with id: {}", id);
        Optional<Order> orderOptional = orderRepository.findById(id);
        return orderMapper.toResponseDto(orderOptional.orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id))));
    }

    @Transactional
    public List<CreateOrderResponse> getMany(List<Long> ids) {
        log.info("Fetching orders with ids: {}", ids);
        List<Order> orders = orderRepository.findAllById(ids);
        return orders.stream()
                .map(orderMapper::toResponseDto)
                .toList();
    }

    @Transactional
    public ResponseEntity<?> create(CreateOrderRequest orderRequest) {
        log.info("Creating order with request: {}", orderRequest);
        CheckOrderRequestDto checkOrderRequestDto = new CheckOrderRequestDto();
        checkOrderRequestDto.setRestaurantId(orderRequest.getRestaurantId());
        checkOrderRequestDto.setDishQuantities(orderRequest.getDishes());

        List<DishDto> dishesListCheckResponse = restaurantClient.getFullListOfDishesIfAvailable(checkOrderRequestDto);

        if (dishesListCheckResponse.isEmpty()) {
            log.warn("Some of the dishes are not available");
            return ResponseEntity.badRequest().body("Some of dishes are not available");
        } else {
            Order order = orderMapper.toEntity(orderRequest);
            order.setUserId(orderRequest.getUserId());
            order.setRestaurantId(orderRequest.getRestaurantId());

            List<OrderDish> orderDishes = dishesListCheckResponse.stream()
                    .map(dishDto -> {
                        OrderDish orderDish = new OrderDish();
                        orderDish.setOrder(order);
                        orderDish.setDish(fetchDishById(dishDto.getId()));
                        orderDish.setQuantity(dishDto.getQuantity());

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
            response.setDishes(dishesListCheckResponse);

            log.info("Order created successfully with id: {}", resultOrder.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }
    }

    @Transactional
    public CreateOrderResponse patch(Long id, JsonNode patchNode) {
        log.info("Patching order with id: {}", id);
        Order order = orderRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id)));

        CreateOrderRequest createOrderRequest = orderMapper.toRequestDto(order);
        createOrderRequest = objectPatcher.patch(createOrderRequest, patchNode);
        orderMapper.updateWithNull(createOrderRequest, order);

        Order resultOrder = orderRepository.save(order);
        log.info("Order patched successfully with id: {}", resultOrder.getId());
        return orderMapper.toResponseDto(resultOrder);
    }

    @Transactional
    public List<Long> patchMany(List<Long> ids, JsonNode patchNode) {
        log.info("Patching orders with ids: {}", ids);
        Collection<Order> orders = orderRepository.findAllById(ids);

        for (Order order : orders) {
            CreateOrderRequest createOrderRequest = orderMapper.toRequestDto(order);
            createOrderRequest = objectPatcher.patch(createOrderRequest, patchNode);
            orderMapper.updateWithNull(createOrderRequest, order);
        }

        List<Order> resultOrders = orderRepository.saveAll(orders);
        log.info("Orders patched successfully with ids: {}", resultOrders.stream().map(Order::getId).toList());
        return resultOrders.stream()
                .map(Order::getId)
                .toList();
    }

    @Transactional
    public Long delete(Long id) {
        log.info("Deleting order with id: {}", id);
        Order order = orderRepository.findById(id).orElse(null);
        if (order != null) {
            orderRepository.delete(order);
            log.info("Order deleted successfully with id: {}", id);
            return id;
        }
        log.warn("Order with id: {} not found", id);
        return null;
    }

    @Transactional
    public void deleteMany(List<Long> ids) {
        log.info("Deleting orders with ids: {}", ids);
        orderRepository.deleteAllById(ids);
        log.info("Orders deleted successfully with ids: {}", ids);
    }

    @Transactional
    public List<CreateOrderResponse> getOrdersByUserId(Long userId) {
        log.info("Fetching orders for user with id: {}", userId);
        return orderRepository.findByUserId(userId).stream()
                .map(orderMapper::toResponseDto)
                .toList();
    }

    @Transactional
    @Override
    public ResponseEntity<?> setOrderStatusPreparing(Long orderId) {
        log.info("Setting order status to preparing for order with id: {}", orderId);
        return updateOrderStatus(orderId, "preparing");
    }

    @Transactional
    @Override
    public ResponseEntity<?> setOrderStatusReady(Long orderId) {
        log.info("Setting order status to ready for order with id: {}", orderId);
        return updateOrderStatus(orderId, "ready");
    }

    @Transactional
    @Override
    public ResponseEntity<?> setOrderStatusDelivered(Long orderId) {
        log.info("Setting order status to delivered for order with id: {}", orderId);
        return updateOrderStatus(orderId, "delivered");
    }

    @Transactional
    private ResponseEntity<?> updateOrderStatus(Long orderId, String status) {
        log.info("Updating order status to {} for order with id: {}", status, orderId);
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order == null) {
            log.warn("Order with id: {} not found", orderId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order not found");
        }
        order.setStatus(status);
        orderRepository.save(order);
        log.info("Order status updated to {} for order with id: {}", status, orderId);
        return ResponseEntity.ok("Order status updated to " + status);
    }

    @Transactional
    private Dish fetchDishById(Long dishId) {
        log.info("Fetching dish with id: {}", dishId);
        return dishRepository.findById(dishId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Dish with id `%s` not found".formatted(dishId)));
    }
}