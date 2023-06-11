package com.samoilov.project.antifraud.repository;

import com.samoilov.project.antifraud.entity.TransactionEntity;
import com.samoilov.project.antifraud.enums.StateCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {

    @Query(value = "SELECT DISTINCT t.ip FROM TransactionEntity t WHERE t.cardNumber = :cardNumber AND t.createdAt >= :startOfPeriod AND t.createdAt <= :endOfPeriod")
    List<String> getAllDistinctIpDuringPeriod(String cardNumber, LocalDateTime startOfPeriod, LocalDateTime endOfPeriod);

    @Query(value = "SELECT DISTINCT t.stateCode FROM TransactionEntity t WHERE t.cardNumber = :cardNumber AND t.createdAt >= :startOfPeriod AND t.createdAt <= :endOfPeriod")
    List<StateCode> getAllDistinctStateCodeDuringPeriod(String cardNumber, LocalDateTime startOfPeriod, LocalDateTime endOfPeriod);

    List<TransactionEntity> getAllByCardNumber(String cardNumber);

}
