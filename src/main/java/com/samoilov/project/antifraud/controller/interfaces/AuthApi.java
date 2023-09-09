package com.samoilov.project.antifraud.controller.interfaces;

import com.samoilov.project.antifraud.dto.ChangeInfoDto;
import com.samoilov.project.antifraud.dto.IpAddressDto;
import com.samoilov.project.antifraud.dto.UserDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@Tag(name = "Auth api")
@RequestMapping("/api/v1/users")
public interface AuthApi {

    @Operation(description = "Create user")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Successfully created user",
                    content = @Content(
                            schema = @Schema(implementation = UserDto.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Wrong user data"),
            @ApiResponse(responseCode = "409", description = "User already exists"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/register")
    ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto);

    @Operation(description = "Get all users")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully got all users",
                    content = @Content(
                            array = @ArraySchema(
                                    schema = @Schema(implementation = IpAddressDto.class)
                            )
                    )
            ),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPPORT')")
    @GetMapping("/list")
    ResponseEntity<List<UserDto>> getAllUsers();

    @Operation(description = "Delete user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted user"),
            @ApiResponse(responseCode = "404", description = "User not found by this username"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{username}")
    ResponseEntity<Map<String, String>> deleteUser(@PathVariable String username);

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully changed user role",
                    content = @Content(
                            schema = @Schema(implementation = UserDto.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Wrong data/invalid role"),
            @ApiResponse(responseCode = "404", description = "User not found by this username"),
            @ApiResponse(responseCode = "409", description = "User already has this role"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/role")
    ResponseEntity<UserDto> changeRole(@Valid @RequestBody ChangeInfoDto roleChangeInfoDto);

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully changed user access"),
            @ApiResponse(responseCode = "400", description = "Wrong data/Impossible to change administrator's lock state"),
            @ApiResponse(responseCode = "404", description = "User not found by this username"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/access")
    ResponseEntity<Map<String, String>> changeAccess(@Valid @RequestBody ChangeInfoDto changeInfoDto);

}
