package com.samoilov.project.antifraud.constant;

import lombok.experimental.UtilityClass;

import java.util.Map;

@UtilityClass
public class AuthConstant {
    public static final String INVALID_ROLE = "Invalid role";
    public static final String USER_ALREADY_HAS_THIS_ROLE = "User already has this role";
    public static final String CHANGING_ADMIN_STATE_EXCEPTION_REASON = "Impossible to change administrator's lock state";
    public static final Map<String, String> USER_WAS_DELETED_RESPONSE = Map.of("status", "User was successfully deleted!");
    public static final Map<String, String> ACCESS_WAS_CHANGED_RESPONSE = Map.of("status", "Access was successfully changed!");
}
