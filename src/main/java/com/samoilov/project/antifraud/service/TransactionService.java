package com.samoilov.project.antifraud.service;

import com.samoilov.project.antifraud.dto.AmountDto;
import com.samoilov.project.antifraud.dto.ResultDto;
import com.samoilov.project.antifraud.enums.PaymentState;
import com.samoilov.project.antifraud.repository.CardNumberRepository;
import com.samoilov.project.antifraud.repository.IpAddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final IpAddressRepository ipAddressRepository;

    private final CardNumberRepository cardNumberRepository;

    public ResultDto prepareTransaction(AmountDto amountDto) {
        List<String> reasons = new ArrayList<>();
        PaymentState paymentState = PaymentState.ALLOWED;

        if (ipAddressRepository.findByIp(amountDto.getIp()).isPresent()) {
            paymentState = PaymentState.PROHIBITED;
            reasons.add("ip");
        }

        if (cardNumberRepository.findByCardNumber(amountDto.getCardNumber()).isPresent()) {
            paymentState = PaymentState.PROHIBITED;
            reasons.add("card-number");
        }

        if (amountDto.getAmount() > 200 && !paymentState.equals(PaymentState.PROHIBITED)) {
            paymentState = amountDto.getAmount() <= 1500
                    ? PaymentState.MANUAL_PROCESSING
                    : PaymentState.PROHIBITED;
            reasons.add("amount");
        } else if (amountDto.getAmount() > 1500) {
            reasons.add("amount");
        }

        return ResultDto.builder().result(paymentState).info(this.joinReasons(reasons)).build();
    }

    private String joinReasons(List<String> reasons) {
        if (reasons.isEmpty()) {
            reasons.add("none");
        }

        reasons.sort(String::compareToIgnoreCase);

        return String.join(", ", reasons);
    }


}

