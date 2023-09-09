package com.samoilov.project.antifraud.service;

import com.samoilov.project.antifraud.dto.CheckResponseDto;
import com.samoilov.project.antifraud.dto.FeedbackDto;
import com.samoilov.project.antifraud.dto.ResultDto;
import com.samoilov.project.antifraud.dto.TransactionDto;
import com.samoilov.project.antifraud.dto.TransactionPreparingInfoDto;
import com.samoilov.project.antifraud.entity.MaxAmountsEntity;
import com.samoilov.project.antifraud.entity.TransactionEntity;
import com.samoilov.project.antifraud.enums.PaymentState;
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

import static com.samoilov.project.antifraud.constant.TransactionConstant.FEEDBACK_ALREADY_EXISTS;
import static com.samoilov.project.antifraud.constant.TransactionConstant.MAX_AMOUNTS_NOT_FOUND;
import static com.samoilov.project.antifraud.constant.TransactionConstant.TRANSACTIONS_NOT_FOUND;
import static com.samoilov.project.antifraud.constant.TransactionConstant.TRANSACTION_NOT_FOUND;
import static com.samoilov.project.antifraud.constant.TransactionConstant.UNKNOWN_PAYMENT_STATE;

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
        return transactionRepository.findAll()
                .stream()
                .map(antifraudInfoMapper::mapTransactionEntityToDto)
                .toList();
    }

    public List<TransactionDto> getHistoryByCardNumber(String cardNumber) {
        List<TransactionEntity> transactionsByCard = transactionRepository.getAllByCardNumber(cardNumber);

        if (transactionsByCard.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, TRANSACTIONS_NOT_FOUND);
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
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, TRANSACTION_NOT_FOUND));

        if (Objects.nonNull(transactionEntity.getFeedback())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, FEEDBACK_ALREADY_EXISTS);
        }

        transactionEntity.setFeedback(feedbackDto.getFeedback());

        this.setMaxAmounts(
                transactionEntity.getPaymentState(), transactionEntity.getFeedback(), transactionEntity.getAmount()
        );

        return antifraudInfoMapper.mapTransactionEntityToDto(transactionRepository.save(transactionEntity));
    }

    @PostConstruct
    @Transactional
    public void createMaxAmounts() {
        if (maxAmountsRepository.findById(1L).isEmpty()) {
            maxAmountsRepository.save(new MaxAmountsEntity());
        }
    }

    @Transactional
    public ResultDto prepareTransaction(TransactionDto transactionDto) {
        LocalDateTime dateTime = LocalDateTime.parse(transactionDto.getDate());
        List<TransactionPreparingInfoDto> distinctIpAndStateCodeInLastHour = transactionRepository
                .getAllDistinctIpAndStateCodeDuringPeriod(transactionDto.getCardNumber(), dateTime)
                .stream()
                .filter(transactionPreparingInfoDto ->
                        !transactionPreparingInfoDto.getIp().equals(transactionDto.getIpAddress())
                        && !transactionPreparingInfoDto.getStateCode().equals(transactionDto.getRegion()))
                .toList();
        CheckResponseDto checkingResponse = this.checkTransaction(distinctIpAndStateCodeInLastHour, transactionDto);

        transactionRepository.save(
                antifraudInfoMapper
                        .mapTransactionDtoToEntity(transactionDto)
                        .addPaymentState(checkingResponse.getPaymentState())
        );

        return ResultDto
                .builder()
                .result(checkingResponse.getPaymentState())
                .info(this.joinReasons(checkingResponse.getReasons()))
                .build();
    }

    private CheckResponseDto checkTransaction(
            List<TransactionPreparingInfoDto> distinctIpAndStateCodeInLastHour,
            TransactionDto transactionDto) {
        List<String> reasons = new ArrayList<>();
        PaymentState paymentState = PaymentState.ALLOWED;
        MaxAmountsEntity maxAmountsEntity = maxAmountsRepository
                .findById(1L)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, MAX_AMOUNTS_NOT_FOUND));

        paymentState = this.changePaymentStatePreparingByDistinctTransactionsLength(
                distinctIpAndStateCodeInLastHour, paymentState, reasons
        );
        paymentState = this.changePaymentStateByAlreadyBannedIpAndCardNumber(transactionDto, paymentState, reasons);
        paymentState = this.changePaymentStateByExceedingLimit(
                paymentState, transactionDto.getAmount(), maxAmountsEntity, reasons
        );

        return CheckResponseDto.builder()
                .reasons(reasons)
                .paymentState(paymentState)
                .build();
    }

    private PaymentState changePaymentStateByExceedingLimit(
            PaymentState paymentState,
            long amount,
            MaxAmountsEntity maxAmountsEntity,
            List<String> reasons) {
        boolean paymentAlreadyProhibited = paymentState.equals(PaymentState.PROHIBITED);
        if (amount > maxAmountsEntity.getMaxAllowedTransactionAmount() && !paymentAlreadyProhibited) {
            paymentState = amount <= maxAmountsEntity.getMaxManualTransactionAmount()
                    ? PaymentState.MANUAL_PROCESSING
                    : PaymentState.PROHIBITED;
            reasons.add("amount");
        } else if (amount > maxAmountsEntity.getMaxManualTransactionAmount() && paymentAlreadyProhibited) {
            reasons.add("amount");
        }
        return paymentState;
    }

    private PaymentState changePaymentStateByAlreadyBannedIpAndCardNumber(
            TransactionDto transactionDto,
            PaymentState paymentState,
            List<String> reasons) {
        if (blockedIpAddressRepository.findByIp(transactionDto.getIpAddress()).isPresent()) {
            paymentState = PaymentState.PROHIBITED;
            reasons.add("ip");
        }

        if (blockedCardNumberRepository.findByCardNumber(transactionDto.getCardNumber()).isPresent()) {
            paymentState = PaymentState.PROHIBITED;
            reasons.add("card-number");
        }

        return paymentState;
    }

    private PaymentState changePaymentStatePreparingByDistinctTransactionsLength(
            List<TransactionPreparingInfoDto> distinctIpAndStateCodeInLastHour,
            PaymentState paymentState,
            List<String> reasons) {
        if (distinctIpAndStateCodeInLastHour.size() >= 2) {
            paymentState = distinctIpAndStateCodeInLastHour.size() == 2
                    ? PaymentState.MANUAL_PROCESSING
                    : PaymentState.PROHIBITED;
            reasons.add("ip-correlation");
        }

        boolean paymentAlreadyProhibited = paymentState.equals(PaymentState.PROHIBITED);
        if (distinctIpAndStateCodeInLastHour.size() >= 2) {
            paymentState = distinctIpAndStateCodeInLastHour.size() == 2 && !paymentAlreadyProhibited
                    ? PaymentState.MANUAL_PROCESSING
                    : PaymentState.PROHIBITED;

            reasons.add("region-correlation");
        }

        return paymentState;
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
        }

        switch (validity) {
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
            default -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST, UNKNOWN_PAYMENT_STATE);
        }
    }

    private void changeAllowedAmount(boolean isToUp, Long valueFromTransaction) {
        Long maxAllowedTransactionAmount = maxAmountsRepository
                .findById(1L)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, MAX_AMOUNTS_NOT_FOUND))
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
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, MAX_AMOUNTS_NOT_FOUND))
                .getMaxManualTransactionAmount();

        double partOfAmount = maxManualTransactionAmount * 0.8;
        double partOfTransaction = valueFromTransaction * 0.2;

        maxAmountsRepository.updateMaxManualTransactionAmount(
                (long) Math.ceil(isToUp ? partOfAmount + partOfTransaction : partOfAmount - partOfTransaction)
        );

        log.warn("Max manual transaction amount was changed to {}", maxManualTransactionAmount);
    }

}


