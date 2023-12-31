package com.samoilov.project.antifraud.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.samoilov.project.antifraud.enums.PaymentState;
import com.samoilov.project.antifraud.enums.StateCode;
import com.samoilov.project.antifraud.validation.annotation.ValidCardNumber;
import com.samoilov.project.antifraud.validation.annotation.ValidIpAddress;
import jakarta.validation.constraints.Min;
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
public class TransactionDto {

    @JsonProperty("transaction_id")
    private Long transactionId;

    @NotNull(message = "Amount must be not null")
    @Min(value = 1, message = "Amount must be greater than 0")
    private Long amount;

    @ValidIpAddress
    @JsonProperty("ip_address")
    private String ipAddress;

    @ValidCardNumber
    @JsonProperty("card_number")
    private String cardNumber;

    private StateCode region;

    private String date;

    private PaymentState result;

    private String feedback;

}
