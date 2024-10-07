package org.tinkoff.restaurantservice.dto.mapper;

import org.mapstruct.*;
import org.tinkoff.restaurantservice.dto.DishDto;
import org.tinkoff.restaurantservice.entity.Dish;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface DishMapper {
    Dish toEntity(DishDto dishDto);

    DishDto toDto(Dish dish);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Dish partialUpdate(DishDto dishDto, @MappingTarget Dish dish);

    Dish updateWithNull(DishDto dishDto, @MappingTarget Dish dish);
}