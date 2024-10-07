package org.tinkoff.userservice.dto;

import lombok.Value;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * DTO for {@link org.tinkoff.userservice.entity.Dish}
 */
@Value
public class DishDto {
    Integer id;
    String name;
    String description;
    BigDecimal price;
    Integer restaurantId;
    Instant createdAt;
    Instant updatedAt;
}