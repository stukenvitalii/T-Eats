package org.tinkoff.restaurantservice.dto;

import lombok.Value;

import java.util.Set;

/**
 * DTO for {@link org.tinkoff.restaurantservice.entity.Restaurant}
 */
@Value
public class RestaurantDto {
    Integer id;
    String name;
    String address;
    String phoneNumber;
    String email;
    Set<DishDto> dishes;
    Set<OrderDto> orders;
}