package com.samoilov.project.antifraud.controller;

import com.samoilov.project.antifraud.components.AntifraudInfoChecker;
import com.samoilov.project.antifraud.dto.CardNumberDto;
import com.samoilov.project.antifraud.dto.IpAddressDto;
import com.samoilov.project.antifraud.service.AntifraudService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/antifraud")
public class AntifraudController {

    private final AntifraudService antifraudService;

    private final AntifraudInfoChecker antifraudInfoChecker;

    @PostMapping("/suspicious-ip")
    public ResponseEntity<IpAddressDto> saveSuspiciousIp(@RequestBody @Valid IpAddressDto ipAddressDto) {
        antifraudInfoChecker.checkIpAddress(ipAddressDto.getIp());
        return ResponseEntity.ok(antifraudService.saveSuspiciousIp(ipAddressDto));
    }

    @DeleteMapping("/suspicious-ip/{ip}")
    public ResponseEntity<Map<String, String>> deleteSuspiciousIp(@PathVariable String ip) {
        antifraudInfoChecker.checkIpAddress(ip);
        return ResponseEntity.ok(antifraudService.deleteSuspiciousIp(ip));
    }

    @GetMapping("/suspicious-ip")
    public ResponseEntity<List<IpAddressDto>> getSuspiciousIp() {
        return ResponseEntity.ok(antifraudService.getAllSuspiciousIp());
    }

    @PostMapping("/stolencard")
    public ResponseEntity<CardNumberDto> saveStolenCard(@RequestBody @Valid CardNumberDto cardNumberDto) {
        antifraudInfoChecker.checkCardNumber(cardNumberDto.getCardNumber());
        return ResponseEntity.ok(antifraudService.saveCardNumber(cardNumberDto));
    }

    @DeleteMapping("/stolencard/{cardNumber}")
    public ResponseEntity<Map<String, String>> deleteStolenCard(@PathVariable String cardNumber) {
        antifraudInfoChecker.checkCardNumber(cardNumber);
        return ResponseEntity.ok(antifraudService.deleteCardNumber(cardNumber));
    }

    @GetMapping("/stolencard")
    public ResponseEntity<List<CardNumberDto>> getStolenCard() {
        return ResponseEntity.ok(antifraudService.getAllCardNumbers());
    }

}
