package org.tinkoff.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * DTO for dish details in an order
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DishDto {
    Long id;
    String name;
    String description;
    BigDecimal price;
    Integer quantity;
}