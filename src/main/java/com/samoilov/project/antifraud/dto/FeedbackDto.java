package com.samoilov.project.antifraud.dto;

import com.samoilov.project.antifraud.enums.PaymentState;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FeedbackDto {

    @Min(value = 0, message = "Feedback must be greater than 0 or equal to 0")
    @NotNull(message = "Transaction id must be not null")
    private Long transactionId;

    @NotNull(message = "Feedback must be not null")
    private PaymentState feedback;
}
