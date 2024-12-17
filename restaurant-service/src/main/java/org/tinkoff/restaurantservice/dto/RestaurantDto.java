package org.tinkoff.restaurantservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO for {@link org.tinkoff.restaurantservice.entity.Restaurant}
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RestaurantDto {
    Long id;
    String name;
    String address;
    String phoneNumber;
    String email;
}