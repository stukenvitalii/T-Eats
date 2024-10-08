package org.tinkoff.orderservice.dto;

import lombok.Value;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * DTO for {@link org.tinkoff.orderservice.entity.Order}
 */
@Value
public class OrderDto {
    Integer id;
    Instant orderDate;
    BigDecimal totalAmount;
    String status;
    Instant createdAt;
    Instant updatedAt;
}