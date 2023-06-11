package com.samoilov.project.antifraud.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StateCode {

    EAP("EAP", "East Asia and Pacific"),

    ECA("ECA", "Europe and Central Asia"),

    HIC("HIC", "High-income countries"),

    LAC("LAC", "Latin America and the Caribbean"),

    MENA("MENA", "The Middle East and North Africa"),

    SA("SA", "South Asia"),

    SSA("SSA", "Sub-Saharan Africa");

    private final String code;

    private final String description;

}

