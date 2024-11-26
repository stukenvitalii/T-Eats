package org.tinkoff.userservice.controller;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.tinkoff.userservice.dto.UserDto;
import org.tinkoff.userservice.service.UserService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/rest/users")
@RequiredArgsConstructor
@Validated
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<UserDto> getUsers() {
        return userService.getList();
    }

    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable("id") Integer id) {
        return userService.getOne(id);
    }

    @GetMapping("/by-ids")
    public List<UserDto> getUsersByIds(@RequestParam List<Integer> ids) {
        return userService.getMany(ids);
    }

    @PostMapping("/add")
    public UserDto createUser(@RequestBody UserDto dto) {
        return userService.create(dto);
    }

    @PatchMapping("/{id}")
    public UserDto patchUser(@PathVariable("id") Integer id, @RequestBody JsonNode patchNode) throws IOException {
        return userService.patch(id, patchNode);
    }

    @PatchMapping
    public List<Integer> patchUsers(@RequestParam List<Integer> ids, @RequestBody JsonNode patchNode) throws IOException {
        return userService.patchMany(ids, patchNode);
    }

    @DeleteMapping("/{id}")
    public UserDto deleteUser(@PathVariable("id") Integer id) {
        return userService.delete(id);
    }

    @DeleteMapping
    public void deleteUsers(@RequestParam List<Integer> ids) {
        userService.deleteMany(ids);
    }
}
