package com.samoilov.project.antifraud.validation;

import com.samoilov.project.antifraud.validation.annotation.ValidIpAddress;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;

public class IpAddressValidator implements ConstraintValidator<ValidIpAddress, String> {
    @Override
    public void initialize(ValidIpAddress constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(
            @Pattern(
                    regexp = "^\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}$",
                    message = "Ip address must be in format xxx.xxx.xxx.xxx"
            )
            @NotNull(message = "Ip address must not be null")
            String ipAddress,
            ConstraintValidatorContext context) {
        Arrays.stream(ipAddress.split("\\."))
                .map(Integer::parseInt)
                .forEach(octet -> {
                    if (octet < 0 || octet > 255) {
                        throw new ResponseStatusException(
                                HttpStatus.BAD_REQUEST, "Ip address octets must be in range 0-255"
                        );
                    }
                });
        return true;
    }

}
