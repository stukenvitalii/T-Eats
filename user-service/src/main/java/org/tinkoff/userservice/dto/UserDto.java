package org.tinkoff.userservice.dto;

import lombok.Value;

/**
 * DTO for {@link org.tinkoff.userservice.entity.User}
 */
@Value
public class UserDto {
    Integer id;
    String username;
    String password;
    String email;
    String firstName;
    String lastName;
}