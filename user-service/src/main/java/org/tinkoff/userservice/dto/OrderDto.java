package org.tinkoff.userservice.dto;

import lombok.Value;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * DTO for {@link org.tinkoff.userservice.entity.Order}
 */
@Value
public class OrderDto {
    Integer id;
    Integer userId;
    Instant orderDate;
    BigDecimal totalAmount;
    String status;
    Instant createdAt;
    Instant updatedAt;
    Integer restaurantId;
}