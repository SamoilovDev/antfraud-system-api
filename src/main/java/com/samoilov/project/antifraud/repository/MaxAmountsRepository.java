package com.samoilov.project.antifraud.repository;

import com.samoilov.project.antifraud.entity.MaxAmountsEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface MaxAmountsRepository extends JpaRepository<MaxAmountsEntity, Long> {

    @Modifying
    @Query("update MaxAmountsEntity m set m.maxAllowedTransactionAmount = :amount where m.id = 1")
    void updateMaxAllowedTransactionAmount(@Param("amount") Long amount);

    @Modifying
    @Transactional
    @Query("update MaxAmountsEntity m set m.maxManualTransactionAmount = :amount where m.id = 1")
    void updateMaxManualTransactionAmount(@Param("amount") Long amount);

}
