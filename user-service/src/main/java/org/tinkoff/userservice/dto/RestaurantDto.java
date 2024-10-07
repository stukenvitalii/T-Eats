package org.tinkoff.userservice.dto;

import lombok.Value;
import org.tinkoff.userservice.entity.Restaurant;

/**
 * DTO for {@link Restaurant}
 */
@Value
public class RestaurantDto {
    Integer id;
    String name;
    String address;
    String phoneNumber;
    String email;
}