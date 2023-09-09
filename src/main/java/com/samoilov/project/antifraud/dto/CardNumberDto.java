package com.samoilov.project.antifraud.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CardNumberDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Min(value = 1, message = "Id must be greater than 0")
    private Long id;

    @JsonProperty(value = "number")
    @NotNull(message = "Card number must not be null")
    private String cardNumber;

}
