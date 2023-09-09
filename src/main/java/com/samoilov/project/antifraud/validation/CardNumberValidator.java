package com.samoilov.project.antifraud.validation;

import com.samoilov.project.antifraud.validation.annotation.ValidCardNumber;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

import static org.apache.logging.log4j.util.Strings.EMPTY;

public class CardNumberValidator implements ConstraintValidator<ValidCardNumber, String> {

    @Override
    public void initialize(ValidCardNumber constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(
            @NotNull(message = "Card number must not be null")
            @Pattern(regexp = "^\\d{16}$", message = "Card number must be 16 digits")
            String cardNumber,
            ConstraintValidatorContext context) {
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

        return sum.get() % 10 == 0;
    }

}
