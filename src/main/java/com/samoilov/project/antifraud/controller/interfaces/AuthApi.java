package com.samoilov.project.antifraud.controller.interfaces;

import com.samoilov.project.antifraud.dto.ChangeInfoDto;
import com.samoilov.project.antifraud.dto.UserDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@Api(tags = "Auth api")
@RequestMapping("/api/auth")
public interface AuthApi {

    @ApiOperation(value = "Get all users")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully got all users", response = UserDto.class, responseContainer = "List"),
            @ApiResponse(code = 500, message = "Internal server error", response = String.class)
    })
    @GetMapping("/list")
    ResponseEntity<List<UserDto>> getAllUsers();

    @ApiOperation(value = "Create user")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully created user", response = UserDto.class),
            @ApiResponse(code = 400, message = "Wrong user data", response = String.class),
            @ApiResponse(code = 409, message = "User already exists", response = String.class),
            @ApiResponse(code = 500, message = "Internal server error", response = String.class)
    })
    @PostMapping("/user")
    ResponseEntity<UserDto> createUser(@RequestBody @Valid UserDto userDto);

    @ApiOperation(value = "Delete user")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully deleted user", response = String.class),
            @ApiResponse(code = 404, message = "User not found by this username", response = String.class),
            @ApiResponse(code = 500, message = "Internal server error", response = String.class)
    })
    @DeleteMapping("/user/{username}")
    ResponseEntity<Map<String, String>> deleteUser(@PathVariable String username);

    @ApiOperation(value = "Change user role")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully changed user role", response = UserDto.class),
            @ApiResponse(code = 400, message = "Wrong data/invalid role", response = String.class),
            @ApiResponse(code = 404, message = "User not found by this username", response = String.class),
            @ApiResponse(code = 409, message = "User already has this role", response = String.class),
            @ApiResponse(code = 500, message = "Internal server error", response = String.class)
    })
    @PutMapping("/role")
    ResponseEntity<UserDto> changeRole(@RequestBody @Valid ChangeInfoDto roleChangeInfoDto);

    @ApiOperation(value = "Change user access")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully changed user access", response = String.class),
            @ApiResponse(code = 400, message = "Wrong data/Impossible to change administrator's lock state", response = String.class),
            @ApiResponse(code = 404, message = "User not found by this username", response = String.class),
            @ApiResponse(code = 500, message = "Internal server error", response = String.class)
    })
    @PutMapping("/access")
    ResponseEntity<Map<String, String>> changeAccess(@RequestBody @Valid ChangeInfoDto changeInfoDto);

}
