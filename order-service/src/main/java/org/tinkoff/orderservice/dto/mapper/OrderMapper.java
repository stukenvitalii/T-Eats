package org.tinkoff.orderservice.dto.mapper;

import org.mapstruct.*;
import org.tinkoff.orderservice.dto.CreateOrderRequest;
import org.tinkoff.orderservice.dto.CreateOrderResponse;
import org.tinkoff.orderservice.dto.DishDto;
import org.tinkoff.orderservice.entity.Order;
import org.tinkoff.orderservice.entity.OrderDish;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrderMapper {
    Order toEntity(CreateOrderRequest createOrderRequest);

    CreateOrderRequest toRequestDto(Order order);

    Order updateWithNull(CreateOrderRequest createOrderRequest, @MappingTarget Order order);

    @Mapping(target = "dishes", expression = "java(mapOrderDishesToDishDtos(order.getOrderDishes()))")
    CreateOrderResponse toResponseDto(Order order);

    default List<DishDto> mapOrderDishesToDishDtos(List<OrderDish> orderDishes) {
        return orderDishes.stream()
                .map(this::mapOrderDishToDishDto)
                .collect(Collectors.toList());
    }

    default DishDto mapOrderDishToDishDto(OrderDish orderDish) {
        DishDto dishDto = new DishDto();
        dishDto.setId(orderDish.getDish().getId());
        dishDto.setName(orderDish.getDish().getName());
        dishDto.setQuantity(orderDish.getQuantity());
        dishDto.setName(orderDish.getDish().getName());
        dishDto.setPrice(orderDish.getDish().getPrice());
        dishDto.setDescription(orderDish.getDish().getDescription());
        return dishDto;
    }
}