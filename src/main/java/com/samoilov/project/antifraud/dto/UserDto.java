package com.samoilov.project.antifraud.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private Long id;

    @NotNull(message = "Name is required!")
    @NotBlank(message = "Name cannot be blank!")
    private String name;

    @NotNull(message = "Username is required!")
    @NotBlank(message = "Username cannot be blank!")
    private String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY, required = true, value = "password")
    private String password;

}
