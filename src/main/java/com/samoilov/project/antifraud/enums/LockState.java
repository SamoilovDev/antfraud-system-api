package com.samoilov.project.antifraud.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LockState {

    LOCK("locked"),

    UNLOCK("unlocked");

    private final String lockState;

}
