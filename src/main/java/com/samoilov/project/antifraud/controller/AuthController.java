package com.samoilov.project.antifraud.controller;

import com.samoilov.project.antifraud.controller.interfaces.AuthApi;
import com.samoilov.project.antifraud.dto.ChangeInfoDto;
import com.samoilov.project.antifraud.dto.UserDto;
import com.samoilov.project.antifraud.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
public class AuthController implements AuthApi {

    private final AuthService authService;

    @Override
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(authService.getAllUsers());
    }

    @Override
    public ResponseEntity<UserDto> createUser(UserDto userDto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(authService.createUser(userDto));
    }

    @Override
    public ResponseEntity<Map<String, String>> deleteUser(String username) {
        return ResponseEntity.ok(authService.deleteUser(username));
    }

    @Override
    public ResponseEntity<UserDto> changeRole(ChangeInfoDto roleChangeInfoDto) {
        return ResponseEntity.ok(authService.changeRole(roleChangeInfoDto));
    }

    @Override
    public ResponseEntity<Map<String, String>> changeAccess(ChangeInfoDto changeInfoDto) {
        return ResponseEntity.ok(authService.changeAccess(changeInfoDto));
    }

}
