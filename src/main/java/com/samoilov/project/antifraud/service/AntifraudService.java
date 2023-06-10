package com.samoilov.project.antifraud.service;

import com.samoilov.project.antifraud.dto.CardNumberDto;
import com.samoilov.project.antifraud.dto.IpAddressDto;
import com.samoilov.project.antifraud.entity.CardNumberEntity;
import com.samoilov.project.antifraud.entity.IpAddressEntity;
import com.samoilov.project.antifraud.mapper.AntifraudInfoMapper;
import com.samoilov.project.antifraud.repository.CardNumberRepository;
import com.samoilov.project.antifraud.repository.IpAddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AntifraudService {

    private final CardNumberRepository cardNumberRepository;

    private final IpAddressRepository ipAddressRepository;

    private final AntifraudInfoMapper antifraudInfoMapper;

    public IpAddressDto saveSuspiciousIp(IpAddressDto ipAddressDto) {
        if (ipAddressRepository.findByIp(ipAddressDto.getIp()).isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Ip address %s already exists".formatted(ipAddressDto.getIp())
            );
        }

        IpAddressEntity ipAddressEntity = ipAddressRepository.save(
                antifraudInfoMapper.mapIpDtoToEntity(ipAddressDto)
        );

        return antifraudInfoMapper.mapIpEntityToDto(ipAddressEntity);
    }

    public Map<String, String> deleteSuspiciousIp(String ip) {
        IpAddressEntity ipAddressEntity = ipAddressRepository
                .findByIp(ip)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ip address %s not found".formatted(ip)));

        ipAddressRepository.delete(ipAddressEntity);

        return Map.of("status", "IP %s successfully removed!".formatted(ip));
    }

    public List<IpAddressDto> getAllSuspiciousIp() {
        List<IpAddressEntity> ipAddressDtoList = ipAddressRepository.findAll();

        return ipAddressDtoList.isEmpty()
                ? List.of()
                : ipAddressDtoList
                .stream()
                .map(antifraudInfoMapper::mapIpEntityToDto)
                .toList();
    }

    public CardNumberDto saveCardNumber(CardNumberDto cardNumberDto) {
        if (cardNumberRepository.findByCardNumber(cardNumberDto.getCardNumber()).isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Card number %s already exists".formatted(cardNumberDto.getCardNumber())
            );
        }

        CardNumberEntity cardNumberEntity = cardNumberRepository.save(
                antifraudInfoMapper.mapCardDtoToEntity(cardNumberDto)
        );

        return antifraudInfoMapper.mapCardEntityToDto(cardNumberEntity);
    }

    public Map<String, String> deleteCardNumber(String cardNumber) {
        CardNumberEntity cardNumberEntity = cardNumberRepository
                .findByCardNumber(cardNumber)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Card with number %s not found".formatted(cardNumber)));

        cardNumberRepository.delete(cardNumberEntity);

        return Map.of("status", "Card %s successfully removed!".formatted(cardNumber));
    }

    public List<CardNumberDto> getAllCardNumbers() {
        return cardNumberRepository
                .findAll()
                .stream()
                .map(antifraudInfoMapper::mapCardEntityToDto)
                .toList();
    }

}

