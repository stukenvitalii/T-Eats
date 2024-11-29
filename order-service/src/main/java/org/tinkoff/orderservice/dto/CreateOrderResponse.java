package org.tinkoff.orderservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Value;

import java.time.Instant;

/**
 * DTO for {@link org.tinkoff.orderservice.entity.Order}
 */
@Value
public class CreateOrderResponse {
    Instant orderDate;
    int totalAmount;
    String status;
    Instant updatedAt;
    @NotNull
    Long userId;
    @NotNull
    Long restaurantId;
}