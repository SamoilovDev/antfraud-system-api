package com.samoilov.project.antifraud.components;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;

@Validated
@Component
public class AntifraudInfoChecker {

    public void checkIpAddress(
            @NotNull(message = "Ip address must not be null")
            @Pattern(regexp = "^\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}$",
                    message = "Ip address must be in format xxx.xxx.xxx.xxx")
            String ip) {
        Arrays.stream(ip.split("\\."))
                .map(Integer::parseInt)
                .forEach(octet -> {
                    if (octet < 0 || octet > 255) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ip address octets must be in range 0-255");
                    }
                });
    }

    public void checkCardNumber(
            @NotNull(message = "Card number must not be null")
            @Pattern(regexp = "^\\d{16}$", message = "Card number must be 16 digits")
            String cardNumber) {
        int sum = 0;

        for (int i = 0; i < cardNumber.length(); i++) {
            int digit = Character.getNumericValue(cardNumber.charAt(i));

            if (i % 2 == 0) {
                digit *= 2;

                if (digit > 9) {
                    digit -= 9;
                }
            }

            sum += digit;
        }

        if (sum % 10 != 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Card number is invalid");
        }
    }
}
