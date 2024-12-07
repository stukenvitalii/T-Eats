package org.tinkoff.restaurantservice.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * DTO for {@link org.tinkoff.restaurantservice.entity.Dish}
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DishDto {
    String name;
    String description;
    BigDecimal price;
    Integer quantity;
}