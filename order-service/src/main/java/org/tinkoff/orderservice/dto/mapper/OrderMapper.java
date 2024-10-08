package org.tinkoff.orderservice.dto.mapper;

import org.mapstruct.*;
import org.tinkoff.orderservice.dto.OrderDto;
import org.tinkoff.orderservice.entity.Order;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrderMapper {
    Order toEntity(OrderDto orderDto);

    OrderDto toDto(Order order);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Order partialUpdate(OrderDto orderDto, @MappingTarget Order order);

    Order updateWithNull(OrderDto orderDto, @MappingTarget Order order);
}