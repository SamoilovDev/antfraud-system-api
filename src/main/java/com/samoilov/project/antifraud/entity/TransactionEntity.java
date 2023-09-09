package com.samoilov.project.antifraud.entity;

import com.samoilov.project.antifraud.enums.PaymentState;
import com.samoilov.project.antifraud.enums.StateCode;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Table(name = "transactions")
public class TransactionEntity {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "amount", nullable = false, updatable = false)
    private Long amount;

    @Column(name = "ip", nullable = false, updatable = false)
    private String ip;

    @Column(name = "card_number", nullable = false, updatable = false)
    private String cardNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "state_code", nullable = false, updatable = false)
    private StateCode stateCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_state", nullable = false, updatable = false)
    private PaymentState paymentState;

    @Enumerated(EnumType.STRING)
    @Column(name = "feedback")
    private PaymentState feedback;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public TransactionEntity addPaymentState(PaymentState paymentState) {
        this.paymentState = paymentState;
        return this;
    }

}
