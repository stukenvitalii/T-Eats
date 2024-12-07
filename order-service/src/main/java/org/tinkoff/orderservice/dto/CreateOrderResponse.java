package org.tinkoff.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

/**
 * DTO for {@link org.tinkoff.orderservice.entity.Order}
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CreateOrderResponse {
    Instant orderDate;
    int totalAmount;
    String status;
    Instant updatedAt;
    Long userId;
    Long restaurantId;
    List<DishDto> dishes;
}