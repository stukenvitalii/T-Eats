package org.tinkoff.orderservice.controller;

import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.tinkoff.orderservice.dto.CreateOrderRequest;
import org.tinkoff.orderservice.dto.CreateOrderResponse;
import org.tinkoff.orderservice.service.OrderService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/rest/orders")
@RequiredArgsConstructor
@Tag(name = "Order Controller", description = "APIs for managing orders")
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    @Operation(summary = "Get list of orders", responses = {
            @ApiResponse(description = "List of orders", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CreateOrderResponse.class)))
    })
    public List<CreateOrderResponse> getOrderList() {
        return orderService.getList();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get order by ID", responses = {
            @ApiResponse(description = "Order details", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CreateOrderResponse.class))),
            @ApiResponse(description = "Order not found", responseCode = "404")
    })
    public CreateOrderResponse getOrderById(@PathVariable Long id) {
        return orderService.getOne(id);
    }

    @GetMapping("/by-ids")
    @Operation(summary = "Get orders by IDs", responses = {
            @ApiResponse(description = "List of orders", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CreateOrderResponse.class)))
    })
    public List<CreateOrderResponse> getOrdersByIds(@RequestParam List<Long> ids) {
        return orderService.getMany(ids);
    }

    @PostMapping("/create")
    @Operation(summary = "Create a new order", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = "application/json", schema = @Schema(implementation = CreateOrderRequest.class))), responses = {
            @ApiResponse(description = "Order created", responseCode = "201", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CreateOrderResponse.class)))
    })
    public CreateOrderResponse createOrder(@RequestBody CreateOrderRequest orderRequest) {
        log.info("Creating order from request: {}", orderRequest);
        log.info("Creating order with restaurantId: {}", orderRequest.getRestaurantId());
        log.info("Creating order with userId: {}", orderRequest.getUserId());
        log.info("Creating order with totalAmount: {}", orderRequest.getTotalAmount());
        return orderService.create(orderRequest);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Patch an order by ID", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = "application/json", schema = @Schema(implementation = JsonNode.class))), responses = {
            @ApiResponse(description = "Order patched", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CreateOrderResponse.class))),
            @ApiResponse(description = "Order not found", responseCode = "404")
    })
    public CreateOrderResponse patchOrder(@PathVariable Long id, @RequestBody JsonNode patchNode) {
        return orderService.patch(id, patchNode);
    }

    @PatchMapping
    @Operation(summary = "Patch multiple orders by IDs", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = "application/json", schema = @Schema(implementation = JsonNode.class))), responses = {
            @ApiResponse(description = "Orders patched", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Integer.class)))
    })
    public List<Long> patchOrders(@RequestParam List<Long> ids, @RequestBody JsonNode patchNode) {
        return orderService.patchMany(ids, patchNode);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an order by ID", responses = {
            @ApiResponse(description = "Order deleted", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CreateOrderResponse.class))),
            @ApiResponse(description = "Order not found", responseCode = "404")
    })
    public CreateOrderResponse deleteOrder(@PathVariable Long id) {
        return orderService.delete(id);
    }

    @DeleteMapping
    @Operation(summary = "Delete multiple orders by IDs", responses = {
            @ApiResponse(description = "Orders deleted", responseCode = "204")
    })
    public void deleteOrders(@RequestParam List<Long> ids) {
        orderService.deleteMany(ids);
    }
}