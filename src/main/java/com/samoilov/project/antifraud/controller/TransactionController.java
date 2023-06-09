package com.samoilov.project.antifraud.controller;

import com.samoilov.project.antifraud.dto.AmountDto;
import com.samoilov.project.antifraud.dto.ResultDto;
import com.samoilov.project.antifraud.service.TransactionService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/antifraud/transaction")
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<ResultDto> prepareTransaction(@RequestBody AmountDto amountDto) {
        return ResponseEntity.ok(transactionService.prepareTransaction(amountDto));
    }

}
