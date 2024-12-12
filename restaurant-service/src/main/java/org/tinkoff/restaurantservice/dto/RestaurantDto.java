package org.tinkoff.restaurantservice.dto;

import lombok.*;

import java.util.Set;

/**
 * DTO for {@link org.tinkoff.restaurantservice.entity.Restaurant}
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RestaurantDto {
    String name;
    String address;
    String phoneNumber;
    String email;
    Set<DishDto> dishes;
    Set<OrderDto> orders;
}