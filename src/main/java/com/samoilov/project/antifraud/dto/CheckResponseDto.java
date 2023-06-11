package com.samoilov.project.antifraud.dto;

import com.samoilov.project.antifraud.enums.PaymentState;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class CheckResponseDto {

    private List<String> reasons;

    private PaymentState paymentState;

}
