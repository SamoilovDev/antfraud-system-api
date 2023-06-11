package com.samoilov.project.antifraud.mapper;

import com.samoilov.project.antifraud.dto.CardNumberDto;
import com.samoilov.project.antifraud.dto.IpAddressDto;
import com.samoilov.project.antifraud.dto.TransactionDto;
import com.samoilov.project.antifraud.entity.CardNumberEntity;
import com.samoilov.project.antifraud.entity.IpAddressEntity;
import com.samoilov.project.antifraud.entity.TransactionEntity;
import com.samoilov.project.antifraud.enums.PaymentState;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import static org.apache.logging.log4j.util.Strings.EMPTY;

@Component
public class AntifraudInfoMapper {

    public IpAddressDto mapIpEntityToDto(IpAddressEntity ipAddressEntity) {
        return IpAddressDto
                .builder()
                .id(ipAddressEntity.getId())
                .ip(ipAddressEntity.getIp())
                .build();
    }

    public IpAddressEntity mapIpDtoToEntity(IpAddressDto ipAddressDto) {
        return IpAddressEntity
                .builder()
                .ip(ipAddressDto.getIp())
                .build();
    }

    public CardNumberDto mapCardEntityToDto(CardNumberEntity cardNumberEntity) {
        return CardNumberDto
                .builder()
                .id(cardNumberEntity.getId())
                .cardNumber(cardNumberEntity.getCardNumber())
                .build();
    }

    public CardNumberEntity mapCardDtoToEntity(CardNumberDto cardNumberDto) {
        return CardNumberEntity
                .builder()
                .cardNumber(cardNumberDto.getCardNumber())
                .build();
    }

    public TransactionEntity mapTransactionDtoToEntity(TransactionDto transactionDto) {
        return TransactionEntity
                .builder()
                .amount(transactionDto.getAmount())
                .cardNumber(transactionDto.getNumber())
                .ip(transactionDto.getIp())
                .stateCode(transactionDto.getRegion())
                .feedback(PaymentState.fromString(transactionDto.getFeedback()))
                .createdAt(LocalDateTime.parse(transactionDto.getDate()))
                .build();
    }

    public TransactionDto mapTransactionEntityToDto(TransactionEntity transactionEntity) {
        return TransactionDto
                .builder()
                .transactionId(transactionEntity.getId())
                .amount(transactionEntity.getAmount())
                .number(transactionEntity.getCardNumber())
                .ip(transactionEntity.getIp())
                .region(transactionEntity.getStateCode())
                .result(transactionEntity.getPaymentState())
                .feedback(
                        Objects.nonNull(transactionEntity.getFeedback())
                                ? transactionEntity.getFeedback().getDescription()
                                : EMPTY
                )
                .date(transactionEntity.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))
                .build();
    }

}

