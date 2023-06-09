package com.samoilov.project.antifraud.controller;

import com.samoilov.project.antifraud.dto.ChangeInfoDto;
import com.samoilov.project.antifraud.dto.UserDto;
import com.samoilov.project.antifraud.entity.UserEntity;
import com.samoilov.project.antifraud.enums.LockState;
import com.samoilov.project.antifraud.service.AuthService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @GetMapping("/list")
    public ResponseEntity<List<UserDto>> getAllUsers(@AuthenticationPrincipal UserEntity userEntity) {
        if (userEntity.getLockState().equals(LockState.LOCK)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You are locked!");
        }
        return ResponseEntity.ok(authService.getAllUsers());
    }

    @PostMapping("/user")
    public ResponseEntity<UserDto> createUser(@RequestBody @Valid UserDto userDto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(authService.createUser(userDto));
    }

    @DeleteMapping("/user/{username}")
    public ResponseEntity<Map<String, String>> deleteUser(@PathVariable String username) {
        return ResponseEntity.ok(authService.deleteUser(username));
    }

    @PutMapping("/role")
    public ResponseEntity<UserDto> changeRole(@RequestBody @Valid ChangeInfoDto roleChangeInfoDto) {
        return ResponseEntity.ok(authService.changeRole(roleChangeInfoDto));
    }

    @PutMapping("/access")
    public ResponseEntity<Map<String, String>> changeAccess(@RequestBody @Valid ChangeInfoDto changeInfoDto) {
        return ResponseEntity.ok(authService.changeAccess(changeInfoDto));
    }

}