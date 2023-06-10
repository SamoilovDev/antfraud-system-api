package com.samoilov.project.antifraud.mapper;

import com.samoilov.project.antifraud.dto.CardNumberDto;
import com.samoilov.project.antifraud.dto.IpAddressDto;
import com.samoilov.project.antifraud.entity.CardNumberEntity;
import com.samoilov.project.antifraud.entity.IpAddressEntity;
import org.springframework.stereotype.Component;

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

}
