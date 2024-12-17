package org.tinkoff.orderservice.controller;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.tinkoff.orderservice.dto.CreateOrderRequest;
import org.tinkoff.orderservice.dto.CreateOrderResponse;
import org.tinkoff.orderservice.service.OrderService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/rest/orders")
@RequiredArgsConstructor
@Validated
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public List<CreateOrderResponse> getOrderList() {
        return orderService.getList();
    }

    @GetMapping("/{id}")
    public CreateOrderResponse getOrderById(@PathVariable("id") Long id) {
        return orderService.getOne(id);
    }

    @GetMapping("/by-ids")
    public List<CreateOrderResponse> getOrdersByIds(@RequestParam("ids") List<Long> ids) {
        return orderService.getMany(ids);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createOrder(@RequestBody CreateOrderRequest orderRequest) {
        return orderService.create(orderRequest);
    }

    @PatchMapping("/{id}")
    public CreateOrderResponse patchOrder(@PathVariable("id") Long id, @RequestBody JsonNode patchNode) {
        return orderService.patch(id, patchNode);
    }

    @PatchMapping
    public List<Long> patchOrders(@RequestParam("ids") List<Long> ids, @RequestBody JsonNode patchNode) {
        return orderService.patchMany(ids, patchNode);
    }

    @DeleteMapping("/{id}")
    public Long deleteOrder(@PathVariable("id") Long id) {
        return orderService.delete(id);
    }

    @DeleteMapping
    public void deleteOrders(@RequestParam List<Long> ids) {
        orderService.deleteMany(ids);
    }

    @GetMapping("/user/{userId}")
    public List<CreateOrderResponse> getOrdersByUserId(@PathVariable Long userId) {
        return orderService.getOrdersByUserId(userId);
    }

    @PatchMapping("/{id}/status/preparing")
    public ResponseEntity<?> setOrderStatusPreparing(@PathVariable("id") Long id) {
        return orderService.setOrderStatusPreparing(id);
    }

    @PatchMapping("/{id}/status/ready")
    public ResponseEntity<?> setOrderStatusReady(@PathVariable("id") Long id) {
        return orderService.setOrderStatusReady(id);
    }

    @PatchMapping("/{id}/status/delivered")
    public ResponseEntity<?> setOrderStatusDelivered(@PathVariable("id") Long id) {
        return orderService.setOrderStatusDelivered(id);
    }
}