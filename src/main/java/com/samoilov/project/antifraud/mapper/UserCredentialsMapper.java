package com.samoilov.project.antifraud.mapper;

import com.samoilov.project.antifraud.dto.UserDto;
import com.samoilov.project.antifraud.entity.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserCredentialsMapper {

    private final PasswordEncoder passwordEncoder;

    public UserDto mapToDto(UserEntity userEntity) {
        return UserDto
                .builder()
                .id(userEntity.getId())
                .name(userEntity.getName())
                .username(userEntity.getUsername())
                .build();
    }

    public UserEntity mapToEntity(UserDto userDto) {
        return UserEntity
                .builder()
                .name(userDto.getName())
                .username(userDto.getUsername())
                .encodedPassword(passwordEncoder.encode(userDto.getPassword()))
                .build();
    }

}
