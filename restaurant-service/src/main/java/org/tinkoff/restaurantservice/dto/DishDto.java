package org.tinkoff.restaurantservice.dto;

import lombok.Value;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * DTO for {@link org.tinkoff.restaurantservice.entity.Dish}
 */
@Value
public class DishDto {
    Integer id;
    String name;
    String description;
    BigDecimal price;
    Instant createdAt;
    Instant updatedAt;
}