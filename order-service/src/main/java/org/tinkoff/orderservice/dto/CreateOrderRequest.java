package org.tinkoff.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO for {@link org.tinkoff.orderservice.entity.Order}
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CreateOrderRequest {
    int totalAmount;
    String status;
    Long userId;
    Long restaurantId;
}