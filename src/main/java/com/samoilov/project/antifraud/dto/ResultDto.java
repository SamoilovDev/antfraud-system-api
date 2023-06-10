package com.samoilov.project.antifraud.dto;

import com.samoilov.project.antifraud.enums.PaymentState;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResultDto {

    @NotNull(message = "Result is required")
    private PaymentState result;

    private String info;

}
