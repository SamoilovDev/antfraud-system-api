package com.samoilov.project.antifraud.service;

import com.samoilov.project.antifraud.dto.AmountDto;
import com.samoilov.project.antifraud.dto.ResultDto;
import com.samoilov.project.antifraud.enums.PaymentState;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

@Service
public class TransactionService {

    public ResultDto prepareTransaction(AmountDto amountDto) {
        if (Objects.isNull(amountDto.getAmount()) || amountDto.getAmount() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        if (amountDto.getAmount() <= 200) {
            return new ResultDto(PaymentState.ALLOWED);
        } else if (amountDto.getAmount() <= 1500) {
            return new ResultDto(PaymentState.MANUAL_PROCESSING);
        } else {
            return new ResultDto(PaymentState.PROHIBITED);
        }
    }


}
