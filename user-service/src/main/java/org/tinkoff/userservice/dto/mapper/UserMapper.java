package org.tinkoff.userservice.dto.mapper;

import org.mapstruct.*;
import org.tinkoff.userservice.dto.UserDto;
import org.tinkoff.userservice.entity.User;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    User toEntity(UserDto userDto);

    UserDto toDto(User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    User partialUpdate(UserDto userDto, @MappingTarget User user);

    User updateWithNull(UserDto userDto, @MappingTarget User user);
}