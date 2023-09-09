package com.samoilov.project.antifraud.controller;

import com.samoilov.project.antifraud.controller.interfaces.TransactionApi;
import com.samoilov.project.antifraud.dto.FeedbackDto;
import com.samoilov.project.antifraud.dto.ResultDto;
import com.samoilov.project.antifraud.dto.TransactionDto;
import com.samoilov.project.antifraud.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TransactionController implements TransactionApi {

    private final TransactionService transactionService;

    @Override
    public ResponseEntity<ResultDto> prepareTransaction(TransactionDto transactionDto) {
        return ResponseEntity.ok(transactionService.prepareTransaction(transactionDto));
    }

    @Override
    public ResponseEntity<TransactionDto> addFeedback(FeedbackDto feedbackDto) {
        return ResponseEntity.ok(transactionService.addFeedback(feedbackDto));
    }

    @Override
    public ResponseEntity<List<TransactionDto>> getFullHistory() {
        return ResponseEntity.ok(transactionService.getFullHistory());
    }

    @Override
    public ResponseEntity<List<TransactionDto>> getHistoryByCardNumber(String number) {
        return ResponseEntity.ok(transactionService.getHistoryByCardNumber(number));
    }

}
