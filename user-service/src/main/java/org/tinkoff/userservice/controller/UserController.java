package org.tinkoff.userservice.controller;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.tinkoff.userservice.dto.UserDto;
import org.tinkoff.userservice.service.UserService;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/rest/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<UserDto> getUsers() {
        return userService.getList();
    }

    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable("id") Long id) {
        return userService.getOne(id);
    }

    @GetMapping("/by-ids")
    public List<UserDto> getUsersByIds(@RequestParam("ids") List<Long> ids) {
        return userService.getMany(ids);
    }

    @PostMapping("/add")
    public UserDto createUser(@RequestBody UserDto dto) {
        return userService.create(dto);
    }

    @PatchMapping("/{id}")
    public UserDto patchUser(@PathVariable("id") Long id, @RequestBody JsonNode patchNode) throws IOException {
        return userService.patch(id, patchNode);
    }

    @PatchMapping
    public List<Long> patchUsers(@RequestParam List<Long> ids, @RequestBody JsonNode patchNode) throws IOException {
        return userService.patchMany(ids, patchNode);
    }

    @DeleteMapping("/{id}")
    public UserDto deleteUser(@PathVariable("id") Long id) {
        return userService.delete(id);
    }

    @DeleteMapping
    public void deleteUsers(@RequestParam List<Long> ids) {
        userService.deleteMany(ids);
    }

    @GetMapping("/check-username")
    public boolean checkUsername(@RequestParam String username) {
        log.info("Checking if username is taken: {}", username);
        return userService.existsByUsername(username);
    }

    @GetMapping("/by-username/{username}")
    public UserDto getUserByUsername(@PathVariable("username") String username) {
        return userService.getUserByUsername(username);
    }
}