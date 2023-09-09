package com.samoilov.project.antifraud.mapper;

import com.samoilov.project.antifraud.dto.CardNumberDto;
import com.samoilov.project.antifraud.dto.IpAddressDto;
import com.samoilov.project.antifraud.dto.TransactionDto;
import com.samoilov.project.antifraud.entity.BlockedCardNumberEntity;
import com.samoilov.project.antifraud.entity.BlockedIpAddressEntity;
import com.samoilov.project.antifraud.entity.TransactionEntity;
import com.samoilov.project.antifraud.enums.PaymentState;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import static org.apache.logging.log4j.util.Strings.EMPTY;

@Component
public class AntifraudInfoMapper {

    public IpAddressDto mapIpEntityToDto(BlockedIpAddressEntity blockedIpAddressEntity) {
        return IpAddressDto.builder()
                .id(blockedIpAddressEntity.getId())
                .ip(blockedIpAddressEntity.getIp())
                .build();
    }

    public BlockedIpAddressEntity mapIpDtoToEntity(IpAddressDto ipAddressDto) {
        return BlockedIpAddressEntity.builder()
                .ip(ipAddressDto.getIp())
                .build();
    }

    public CardNumberDto mapCardEntityToDto(BlockedCardNumberEntity blockedCardNumberEntity) {
        return CardNumberDto.builder()
                .id(blockedCardNumberEntity.getId())
                .cardNumber(blockedCardNumberEntity.getCardNumber())
                .build();
    }

    public BlockedCardNumberEntity mapCardDtoToEntity(CardNumberDto cardNumberDto) {
        return BlockedCardNumberEntity.builder()
                .cardNumber(cardNumberDto.getCardNumber())
                .build();
    }

    public TransactionEntity mapTransactionDtoToEntity(TransactionDto transactionDto) {
        return TransactionEntity.builder()
                .amount(transactionDto.getAmount())
                .cardNumber(transactionDto.getCardNumber())
                .ip(transactionDto.getIpAddress())
                .stateCode(transactionDto.getRegion())
                .feedback(PaymentState.fromString(transactionDto.getFeedback()))
                .createdAt(LocalDateTime.parse(transactionDto.getDate()))
                .build();
    }

    public TransactionDto mapTransactionEntityToDto(TransactionEntity transactionEntity) {
        return TransactionDto.builder()
                .transactionId(transactionEntity.getId())
                .amount(transactionEntity.getAmount())
                .cardNumber(transactionEntity.getCardNumber())
                .ipAddress(transactionEntity.getIp())
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

