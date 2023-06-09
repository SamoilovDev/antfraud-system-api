package com.samoilov.project.antifraud.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class ChangeInfoDto {

    @NotNull(message = "Username is required!")
    @NotBlank(message = "Username cannot be blank!")
    private String username;

    @JsonAlias({"role", "operation"})
    @NotNull(message = "Role/operation is required!")
    @NotBlank(message = "Role/operation cannot be blank!")
    private String role;

}
