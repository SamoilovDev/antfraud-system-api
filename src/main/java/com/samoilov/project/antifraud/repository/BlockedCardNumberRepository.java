package com.samoilov.project.antifraud.repository;

import com.samoilov.project.antifraud.entity.CardNumberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BlockedCardNumberRepository extends JpaRepository<CardNumberEntity, Long> {

    Optional<CardNumberEntity> findByCardNumber(String cardNumber);

}
