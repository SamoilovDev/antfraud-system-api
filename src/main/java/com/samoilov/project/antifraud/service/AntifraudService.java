package com.samoilov.project.antifraud.service;

import com.samoilov.project.antifraud.dto.CardNumberDto;
import com.samoilov.project.antifraud.dto.IpAddressDto;
import com.samoilov.project.antifraud.entity.BlockedCardNumberEntity;
import com.samoilov.project.antifraud.entity.BlockedIpAddressEntity;
import com.samoilov.project.antifraud.mapper.AntifraudInfoMapper;
import com.samoilov.project.antifraud.repository.BlockedCardNumberRepository;
import com.samoilov.project.antifraud.repository.BlockedIpAddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

import static com.samoilov.project.antifraud.constant.AntifraudConstant.CARD_NUMBER_ALREADY_STOLEN;
import static com.samoilov.project.antifraud.constant.AntifraudConstant.CARD_NUMBER_IS_NOT_STOLEN;
import static com.samoilov.project.antifraud.constant.AntifraudConstant.DELETE_STOLEN_CARD_RESPONSE;
import static com.samoilov.project.antifraud.constant.AntifraudConstant.DELETE_SUSPICIOUS_IP_RESPONSE;
import static com.samoilov.project.antifraud.constant.AntifraudConstant.IP_ADDRESS_ALREADY_SUSPICIOUS;
import static com.samoilov.project.antifraud.constant.AntifraudConstant.IP_ADDRESS_IS_NOT_SUSPICIOUS;

@Service
@RequiredArgsConstructor
public class AntifraudService {

    private final BlockedCardNumberRepository blockedCardNumberRepository;

    private final BlockedIpAddressRepository blockedIpAddressRepository;

    private final AntifraudInfoMapper antifraudInfoMapper;

    public IpAddressDto saveSuspiciousIp(IpAddressDto ipAddressDto) {
        if (blockedIpAddressRepository.findByIp(ipAddressDto.getIp()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, IP_ADDRESS_ALREADY_SUSPICIOUS);
        }

        Long savedIpId = blockedIpAddressRepository
                .save(antifraudInfoMapper.mapIpDtoToEntity(ipAddressDto))
                .getId();

        return ipAddressDto.toBuilder()
                .id(savedIpId)
                .build();
    }

    public CardNumberDto saveCardNumber(CardNumberDto cardNumberDto) {
        if (blockedCardNumberRepository.findByCardNumber(cardNumberDto.getCardNumber()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, CARD_NUMBER_ALREADY_STOLEN);
        }

        Long savedCardNumberId = blockedCardNumberRepository
                .save(antifraudInfoMapper.mapCardDtoToEntity(cardNumberDto))
                .getId();

        return cardNumberDto.toBuilder()
                .id(savedCardNumberId)
                .build();
    }

    public Map<String, String> deleteSuspiciousIp(String ip) {
        BlockedIpAddressEntity blockedIpAddressEntity = blockedIpAddressRepository
                .findByIp(ip)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, IP_ADDRESS_IS_NOT_SUSPICIOUS));

        blockedIpAddressRepository.delete(blockedIpAddressEntity);

        return DELETE_SUSPICIOUS_IP_RESPONSE;
    }


    public Map<String, String> deleteCardNumber(String cardNumber) {
        BlockedCardNumberEntity blockedCardNumberEntity = blockedCardNumberRepository
                .findByCardNumber(cardNumber)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, CARD_NUMBER_IS_NOT_STOLEN));

        blockedCardNumberRepository.delete(blockedCardNumberEntity);

        return DELETE_STOLEN_CARD_RESPONSE;
    }

    public List<CardNumberDto> getAllCardNumbers() {
        return blockedCardNumberRepository.findAll()
                .stream()
                .map(antifraudInfoMapper::mapCardEntityToDto)
                .toList();
    }

    public List<IpAddressDto> getAllSuspiciousIp() {
        return blockedIpAddressRepository.findAll()
                .stream()
                .map(antifraudInfoMapper::mapIpEntityToDto)
                .toList();
    }

}


