package com.samoilov.project.antifraud.components;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

import static org.apache.logging.log4j.util.Strings.EMPTY;

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
                        throw new ResponseStatusException(
                                HttpStatus.BAD_REQUEST, "Ip address octets must be in range 0-255"
                        );
                    }
                });
    }

    public void checkCardNumber(
            @NotNull(message = "Card number must not be null")
            @Pattern(regexp = "^\\d{16}$", message = "Card number must be 16 digits")
            String cardNumber) {
        AtomicInteger sum = new AtomicInteger(0);
        AtomicInteger index = new AtomicInteger(0);

        Arrays.stream(cardNumber.split(EMPTY))
                .map(Integer::parseInt)
                .forEach(num -> {
                    if (index.getAndIncrement() % 2 == 0) {
                        num *= 2;
                        if (num > 9) {
                            num -= 9;
                        }
                    }
                    sum.addAndGet(num);
                });

        if (sum.get() % 10 != 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Card number is invalid");
        }
    }
}
