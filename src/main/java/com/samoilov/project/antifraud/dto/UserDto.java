package com.samoilov.project.antifraud.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.samoilov.project.antifraud.enums.Authority;
import com.samoilov.project.antifraud.enums.LockState;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {

    @Min(value = 1, message = "Id must be greater than 0!")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotNull(message = "Name is required!")
    @NotBlank(message = "Name cannot be blank!")
    private String name;

    @NotNull(message = "Username is required!")
    @NotBlank(message = "Username cannot be blank!")
    private String username;

    @NotNull(message = "Password is required!")
    @NotBlank(message = "Password cannot be blank!")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY, required = true, value = "password")
    private String password;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY, required = true)
    private Authority role;

    @JsonIgnore
    private LockState lockState;

}
