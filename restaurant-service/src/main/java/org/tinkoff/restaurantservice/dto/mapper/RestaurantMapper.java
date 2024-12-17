package org.tinkoff.restaurantservice.dto.mapper;

import org.mapstruct.*;
import org.tinkoff.restaurantservice.dto.RestaurantDto;
import org.tinkoff.restaurantservice.entity.Restaurant;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface RestaurantMapper {
    Restaurant toEntity(RestaurantDto restaurantDto);

    RestaurantDto toDto(Restaurant restaurant);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Restaurant partialUpdate(RestaurantDto restaurantDto, @MappingTarget Restaurant restaurant);

    Restaurant updateWithNull(RestaurantDto restaurantDto, @MappingTarget Restaurant restaurant);
}