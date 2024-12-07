package org.tinkoff.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CheckOrderRequestDto {
    Long restaurantId;
    Map<Long, Integer> dishQuantities; // Map of dish ID to quantity
}
