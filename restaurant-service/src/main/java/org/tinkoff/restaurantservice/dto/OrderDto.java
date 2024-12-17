package org.tinkoff.restaurantservice.dto;

import lombok.Value;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * DTO for {@link org.tinkoff.restaurantservice.entity.Order}
 */
@Value
public class OrderDto {
    Long id;
    Instant orderDate;
    BigDecimal totalAmount;
    String status;
    Instant updatedAt;
}