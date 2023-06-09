package com.samoilov.project.antifraud.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Authority {

    ADMINISTRATOR("ADMINISTRATOR"),

    MERCHANT("MERCHANT"),

    SUPPORT("SUPPORT");

    private final String authority;

}
