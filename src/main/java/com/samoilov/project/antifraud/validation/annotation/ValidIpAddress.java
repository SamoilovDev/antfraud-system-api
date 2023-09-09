package com.samoilov.project.antifraud.validation.annotation;

import com.samoilov.project.antifraud.validation.IpAddressValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = IpAddressValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
public @interface ValidIpAddress {

    String message() default "Invalid IP address format";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
