package org.tinkoff.orderservice.dto.mapper;

import org.mapstruct.*;
import org.tinkoff.orderservice.dto.CreateOrderRequest;
import org.tinkoff.orderservice.dto.CreateOrderResponse;
import org.tinkoff.orderservice.entity.Order;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrderMapper {
    Order toEntity(CreateOrderRequest createOrderRequest);

    CreateOrderRequest toRequestDto(Order order);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Order partialUpdate(CreateOrderRequest createOrderRequest, @MappingTarget Order order);

    Order updateWithNull(CreateOrderRequest createOrderRequest, @MappingTarget Order order);

    CreateOrderResponse toResponseDto(Order order);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Order partialUpdate(CreateOrderResponse createOrderResponse, @MappingTarget Order order);
}