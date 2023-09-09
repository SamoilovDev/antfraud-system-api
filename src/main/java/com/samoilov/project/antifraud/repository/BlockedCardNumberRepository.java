package com.samoilov.project.antifraud.repository;

import com.samoilov.project.antifraud.entity.BlockedCardNumberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BlockedCardNumberRepository extends JpaRepository<BlockedCardNumberEntity, Long> {

    Optional<BlockedCardNumberEntity> findByCardNumber(String cardNumber);

}
