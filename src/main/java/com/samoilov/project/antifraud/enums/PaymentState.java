package com.samoilov.project.antifraud.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

@Getter
@RequiredArgsConstructor
public enum PaymentState {

    ALLOWED("ALLOWED"),

    MANUAL_PROCESSING("MANUAL_PROCESSING"),

    PROHIBITED("PROHIBITED");

    private final String description;

    public static PaymentState fromString(String text) {
        if (Objects.isNull(text)) return null;

        String preparedText = text.trim();

        for (PaymentState paymentState : PaymentState.values()) {
            if (paymentState.description.equalsIgnoreCase(preparedText)) {
                return paymentState;
            }
        }

        return null;
    }

}
