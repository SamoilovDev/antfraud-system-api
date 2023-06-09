package com.samoilov.project.antifraud.handler;

import com.samoilov.project.antifraud.dto.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ValidateHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity
                .badRequest()
                .body(
                        ApiError.builder()
                                .error("Validation error")
                                .status(HttpStatus.BAD_REQUEST)
                                .message(e.getMessage())
                                .build()
                );
    }

}
