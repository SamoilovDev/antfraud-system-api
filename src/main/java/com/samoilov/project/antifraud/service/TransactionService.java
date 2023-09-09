package com.samoilov.project.antifraud.service;

import com.samoilov.project.antifraud.dto.CheckResponseDto;
import com.samoilov.project.antifraud.dto.FeedbackDto;
import com.samoilov.project.antifraud.dto.ResultDto;
import com.samoilov.project.antifraud.dto.TransactionDto;
import com.samoilov.project.antifraud.entity.MaxAmountsEntity;
import com.samoilov.project.antifraud.entity.TransactionEntity;
import com.samoilov.project.antifraud.enums.PaymentState;
import com.samoilov.project.antifraud.enums.StateCode;
import com.samoilov.project.antifraud.mapper.AntifraudInfoMapper;
import com.samoilov.project.antifraud.repository.BlockedCardNumberRepository;
import com.samoilov.project.antifraud.repository.BlockedIpAddressRepository;
import com.samoilov.project.antifraud.repository.MaxAmountsRepository;
import com.samoilov.project.antifraud.repository.TransactionRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionService {

    private final BlockedIpAddressRepository blockedIpAddressRepository;

    private final BlockedCardNumberRepository blockedCardNumberRepository;

    private final TransactionRepository transactionRepository;

    private final MaxAmountsRepository maxAmountsRepository;

    private final AntifraudInfoMapper antifraudInfoMapper;

    public List<TransactionDto> getFullHistory() {
        return transactionRepository
                .findAll()
                .stream()
                .map(antifraudInfoMapper::mapTransactionEntityToDto)
                .toList();
    }

    public List<TransactionDto> getHistoryByCardNumber(String cardNumber) {
        List<TransactionEntity> transactionsByCard = transactionRepository.getAllByCardNumber(cardNumber);

        if (transactionsByCard.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Transactions with this card number not found!");
        }

        return transactionsByCard
                .stream()
                .map(antifraudInfoMapper::mapTransactionEntityToDto)
                .toList();
    }

    @Transactional
    public TransactionDto addFeedback(FeedbackDto feedbackDto) {
        TransactionEntity transactionEntity = transactionRepository
                .findById(feedbackDto.getTransactionId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Transaction not found"));

        if (Objects.nonNull(transactionEntity.getFeedback())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Feedback already exists");
        }

        transactionEntity.setFeedback(feedbackDto.getFeedback());

        this.setMaxAmounts(
                transactionEntity.getPaymentState(), transactionEntity.getFeedback(), transactionEntity.getAmount()
        );

        return antifraudInfoMapper.mapTransactionEntityToDto(transactionRepository.save(transactionEntity));
    }

    public ResultDto prepareTransaction(TransactionDto transactionDto) {
        LocalDateTime dateTime = LocalDateTime.parse(transactionDto.getDate());

        List<String> distinctIpInLastHour = transactionRepository
                .getAllDistinctIpDuringPeriod(
                        transactionDto.getCardNumber(), dateTime.minusHours(1), dateTime
                )
                .stream()
                .filter(ipAddress -> !ipAddress.equals(transactionDto.getIpAddress()))
                .toList();
        List<StateCode> distinctStateCodesInLastHour = transactionRepository
                .getAllDistinctStateCodeDuringPeriod(
                        transactionDto.getCardNumber(), dateTime.minusHours(1), dateTime
                )
                .stream()
                .filter(stateCode -> !stateCode.equals(transactionDto.getRegion()))
                .toList();

        CheckResponseDto checkingResponse = this.checkTransaction(
                distinctIpInLastHour, distinctStateCodesInLastHour, transactionDto
        );

        transactionRepository.save(
                antifraudInfoMapper
                        .mapTransactionDtoToEntity(transactionDto)
                        .getThisWithPaymentState(checkingResponse.getPaymentState())
        );

        return ResultDto
                .builder()
                .result(checkingResponse.getPaymentState())
                .info(this.joinReasons(checkingResponse.getReasons()))
                .build();
    }

    private CheckResponseDto checkTransaction(
            List<String> distinctIpInLastHour,
            List<StateCode> distinctStateCodesInLastHour,
            TransactionDto transactionDto) {
        Long amount = transactionDto.getAmount();
        List<String> reasons = new ArrayList<>();
        PaymentState paymentState = PaymentState.ALLOWED;
        MaxAmountsEntity maxAmountsEntity = maxAmountsRepository
                .findById(1L)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Max amounts not found"));

        if (distinctIpInLastHour.size() >= 2) {
            paymentState = distinctIpInLastHour.size() == 2 ? PaymentState.MANUAL_PROCESSING : PaymentState.PROHIBITED;
            reasons.add("ip-correlation");
        }

        boolean paymentAlreadyProhibited = paymentState.equals(PaymentState.PROHIBITED);
        if (distinctStateCodesInLastHour.size() >= 2) {
            paymentState = distinctStateCodesInLastHour.size() == 2 && !paymentAlreadyProhibited
                    ? PaymentState.MANUAL_PROCESSING
                    : PaymentState.PROHIBITED;

            reasons.add("region-correlation");
        }

        if (blockedIpAddressRepository.findByIp(transactionDto.getIpAddress()).isPresent()) {
            paymentState = PaymentState.PROHIBITED;
            reasons.add("ip");
        }

        if (blockedCardNumberRepository.findByCardNumber(transactionDto.getCardNumber()).isPresent()) {
            paymentState = PaymentState.PROHIBITED;
            reasons.add("card-number");
        }

        paymentAlreadyProhibited = paymentState.equals(PaymentState.PROHIBITED);
        if (amount > maxAmountsEntity.getMaxAllowedTransactionAmount() && !paymentAlreadyProhibited) {
            paymentState = amount <= maxAmountsEntity.getMaxManualTransactionAmount()
                    ? PaymentState.MANUAL_PROCESSING
                    : PaymentState.PROHIBITED;
            reasons.add("amount");
        } else if (amount > maxAmountsEntity.getMaxManualTransactionAmount() && paymentAlreadyProhibited) {
            reasons.add("amount");
        }


        return CheckResponseDto
                .builder()
                .reasons(reasons)
                .paymentState(paymentState)
                .build();
    }

    private String joinReasons(List<String> reasons) {
        if (reasons.isEmpty()) {
            reasons.add("none");
        }
        reasons.sort(String::compareToIgnoreCase);
        return String.join(", ", reasons);
    }

    private void setMaxAmounts(PaymentState validity, PaymentState feedback, Long valueFromTransaction) {
        if (validity.equals(feedback)) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
        } else switch (validity) {
            case ALLOWED -> {
                if (feedback.equals(PaymentState.PROHIBITED)) {
                    this.changeManualAmount(Boolean.FALSE, valueFromTransaction);
                }
                this.changeAllowedAmount(Boolean.FALSE, valueFromTransaction);
            }
            case MANUAL_PROCESSING -> {
                if (feedback.equals(PaymentState.ALLOWED)) {
                    this.changeAllowedAmount(Boolean.TRUE, valueFromTransaction);
                } else {
                    this.changeManualAmount(Boolean.FALSE, valueFromTransaction);
                }
            }
            case PROHIBITED -> {
                if (feedback.equals(PaymentState.MANUAL_PROCESSING)) {
                    this.changeAllowedAmount(Boolean.TRUE, valueFromTransaction);
                }
                this.changeManualAmount(Boolean.TRUE, valueFromTransaction);
            }
            default -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unknown payment state");
        }
    }

    private void changeAllowedAmount(boolean isToUp, Long valueFromTransaction) {
        Long maxAllowedTransactionAmount = maxAmountsRepository
                .findById(1L)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Max amounts not found"))
                .getMaxAllowedTransactionAmount();

        double partOfAmount = maxAllowedTransactionAmount * 0.8;
        double partOfTransaction = valueFromTransaction * 0.2;

        maxAmountsRepository.updateMaxAllowedTransactionAmount(
                (long) Math.ceil(isToUp ? partOfAmount + partOfTransaction : partOfAmount - partOfTransaction)
        );

        log.info("Max allowed transaction amount was changed to {}", maxAllowedTransactionAmount);
    }

    private void changeManualAmount(boolean isToUp, Long valueFromTransaction) {
        Long maxManualTransactionAmount = maxAmountsRepository
                .findById(1L)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Max amounts not found"))
                .getMaxManualTransactionAmount();

        double partOfAmount = maxManualTransactionAmount * 0.8;
        double partOfTransaction = valueFromTransaction * 0.2;

        maxAmountsRepository.updateMaxManualTransactionAmount(
                (long) Math.ceil(isToUp ? partOfAmount + partOfTransaction : partOfAmount - partOfTransaction)
        );

        log.warn("Max manual transaction amount was changed to {}", maxManualTransactionAmount);
    }

    @PostConstruct
    public void createMaxAmounts() {
        if (maxAmountsRepository.findById(1L).isEmpty()) {
            maxAmountsRepository.save(
                    MaxAmountsEntity
                            .builder()
                            .id(1L)
                            .maxManualTransactionAmount(1500L)
                            .maxAllowedTransactionAmount(200L)
                            .build()
            );
        }
    }

}


