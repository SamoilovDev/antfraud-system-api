package com.samoilov.project.antifraud.repository;

import com.samoilov.project.antifraud.entity.CardNumberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BlockedCardNumberRepository extends JpaRepository<CardNumberEntity, Long> {

    Optional<CardNumberEntity> findByCardNumber(String cardNumber);

}
