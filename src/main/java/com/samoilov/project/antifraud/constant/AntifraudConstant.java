package com.samoilov.project.antifraud.constant;

import lombok.experimental.UtilityClass;

import java.util.Map;

@UtilityClass
public class AntifraudConstant {
    public static final Map<String, String> DELETE_SUSPICIOUS_IP_RESPONSE = Map.of("status", "Ip address was successfully removed!");
    public static final Map<String, String> DELETE_STOLEN_CARD_RESPONSE = Map.of("status", "Card number was successfully removed!");
    public static final String IP_ADDRESS_ALREADY_SUSPICIOUS = "This ip address already suspicious";
    public static final String IP_ADDRESS_IS_NOT_SUSPICIOUS = "Ip address is not suspicious";
    public static final String CARD_NUMBER_ALREADY_STOLEN = "This card number already stolen";
    public static final String CARD_NUMBER_IS_NOT_STOLEN = "Card number is not stolen";
}
