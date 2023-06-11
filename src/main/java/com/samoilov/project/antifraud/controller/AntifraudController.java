package com.samoilov.project.antifraud.controller;

import com.samoilov.project.antifraud.components.AntifraudInfoChecker;
import com.samoilov.project.antifraud.controller.interfaces.AntifraudApi;
import com.samoilov.project.antifraud.dto.CardNumberDto;
import com.samoilov.project.antifraud.dto.IpAddressDto;
import com.samoilov.project.antifraud.service.AntifraudService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class AntifraudController implements AntifraudApi {

    private final AntifraudService antifraudService;

    private final AntifraudInfoChecker antifraudInfoChecker;

    @Override
    public ResponseEntity<IpAddressDto> saveSuspiciousIp(IpAddressDto ipAddressDto) {
        antifraudInfoChecker.checkIpAddress(ipAddressDto.getIp());
        return ResponseEntity.ok(antifraudService.saveSuspiciousIp(ipAddressDto));
    }

    @Override
    public ResponseEntity<Map<String, String>> deleteSuspiciousIp(String ip) {
        antifraudInfoChecker.checkIpAddress(ip);
        return ResponseEntity.ok(antifraudService.deleteSuspiciousIp(ip));
    }

    @Override
    public ResponseEntity<List<IpAddressDto>> getAllSuspiciousIp() {
        return ResponseEntity.ok(antifraudService.getAllSuspiciousIp());
    }

    @Override
    public ResponseEntity<CardNumberDto> saveStolenCard(CardNumberDto cardNumberDto) {
        antifraudInfoChecker.checkCardNumber(cardNumberDto.getCardNumber());
        return ResponseEntity.ok(antifraudService.saveCardNumber(cardNumberDto));
    }

    @Override
    public ResponseEntity<Map<String, String>> deleteStolenCard(String cardNumber) {
        antifraudInfoChecker.checkCardNumber(cardNumber);
        return ResponseEntity.ok(antifraudService.deleteCardNumber(cardNumber));
    }

    @Override
    public ResponseEntity<List<CardNumberDto>> getAllStolenCards() {
        return ResponseEntity.ok(antifraudService.getAllCardNumbers());
    }

}