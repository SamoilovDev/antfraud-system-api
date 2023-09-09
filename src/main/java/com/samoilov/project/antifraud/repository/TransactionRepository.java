package com.samoilov.project.antifraud.repository;

import com.samoilov.project.antifraud.dto.TransactionPreparingInfoDto;
import com.samoilov.project.antifraud.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {

    @Query(value = "SELECT DISTINCT t.ip, t.state_code FROM antifraud.transactions t " +
            "WHERE t.card_number = :cardNumber " +
            "AND t.created_at  >= DATE_SUB(:endOfPeriod, INTERVAL 1 HOUR)  " +
            "AND t.created_at <= :endOfPeriod", nativeQuery = true)
    List<TransactionPreparingInfoDto> getAllDistinctIpAndStateCodeDuringPeriod(
            String cardNumber, LocalDateTime endOfPeriod
    );

    List<TransactionEntity> getAllByCardNumber(String cardNumber);

}
