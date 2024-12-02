package org.tinkoff.userservice.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.tinkoff.userservice.dto.UserDto;

import java.io.IOException;
import java.util.List;

public interface UserService {
    List<UserDto> getList();

    UserDto getOne(Long id);

    List<UserDto> getMany(List<Long> ids);

    UserDto create(UserDto dto);

    UserDto patch(Long id, JsonNode patchNode) throws IOException;

    List<Long> patchMany(List<Long> ids, JsonNode patchNode) throws IOException;

    UserDto delete(Long id);

    void deleteMany(List<Long> ids);

    UserDto getUserByUsername(String username);

    boolean existsByUsername(String username);
}
