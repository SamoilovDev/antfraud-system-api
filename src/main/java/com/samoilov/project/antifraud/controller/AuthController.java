package com.samoilov.project.antifraud.controller;

import com.samoilov.project.antifraud.dto.UserDto;
import com.samoilov.project.antifraud.service.AuthService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @GetMapping("/list")
    @PreAuthorize(value = "authenticated()")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(authService.getAllUsers());
    }

    @PostMapping("/user")
    @PreAuthorize(value = "permitAll()")
    public ResponseEntity<UserDto> createUser(@RequestBody @Valid UserDto userDto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(authService.createUser(userDto));
    }

    @DeleteMapping("/user/{username}")
    @PreAuthorize(value = "authenticated()")
    public ResponseEntity<Map<String, String>> deleteUser(@PathVariable String username) {
        return ResponseEntity.ok(authService.deleteUser(username));
    }

}
