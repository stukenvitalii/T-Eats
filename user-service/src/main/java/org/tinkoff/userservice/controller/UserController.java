package org.tinkoff.userservice.controller;

import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Get all users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the users"),
            @ApiResponse(responseCode = "404", description = "Users not found")
    })
    @GetMapping
    public List<UserDto> getUsers() {
        return userService.getList();
    }

    @Operation(summary = "Get a user by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the user"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable("id") Long id) {
        return userService.getOne(id);
    }

    @Operation(summary = "Get users by IDs")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the users"),
            @ApiResponse(responseCode = "404", description = "Users not found")
    })
    @GetMapping("/by-ids")
    public List<UserDto> getUsersByIds(@RequestParam("ids") List<Long> ids) {
        return userService.getMany(ids);
    }

    @Operation(summary = "Create a new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping("/add")
    public UserDto createUser(@RequestBody UserDto dto) {
        return userService.create(dto);
    }

    @Operation(summary = "Update a user by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PatchMapping("/{id}")
    public UserDto patchUser(@PathVariable("id") Long id, @RequestBody JsonNode patchNode) throws IOException {
        return userService.patch(id, patchNode);
    }

    @Operation(summary = "Update multiple users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users updated"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Users not found")
    })
    @PatchMapping
    public List<Long> patchUsers(@RequestParam List<Long> ids, @RequestBody JsonNode patchNode) throws IOException {
        return userService.patchMany(ids, patchNode);
    }

    @Operation(summary = "Delete a user by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User deleted"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @DeleteMapping("/{id}")
    public UserDto deleteUser(@PathVariable("id") Long id) {
        return userService.delete(id);
    }

    @Operation(summary = "Delete multiple users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users deleted"),
            @ApiResponse(responseCode = "404", description = "Users not found")
    })
    @DeleteMapping
    public void deleteUsers(@RequestParam List<Long> ids) {
        userService.deleteMany(ids);
    }

    @Operation(summary = "Check if username exists")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Username exists"),
            @ApiResponse(responseCode = "404", description = "Username not found")
    })
    @GetMapping("/check-username")
    public boolean checkUsername(@RequestParam String username) {
        log.info("Checking if username is taken: {}", username);
        return userService.existsByUsername(username);
    }

    @Operation(summary = "Get a user by username")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the user"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/by-username/{username}")
    public UserDto getUserByUsername(@PathVariable("username") String username) {
        return userService.getUserByUsername(username);
    }
}