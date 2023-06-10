package com.samoilov.project.antifraud.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AmountDto {

    @NotNull(message = "Amount must not be null")
    @Min(value = 1, message = "Amount must be greater than 0")
    private Long amount;

    private String ip;

    @JsonProperty("number")
    private String cardNumber;

}

