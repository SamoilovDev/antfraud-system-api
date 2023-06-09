package com.samoilov.project.antifraud.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Optional;

@Getter
@AllArgsConstructor
public enum Authority implements GrantedAuthority {

    ADMINISTRATOR("ADMINISTRATOR"),

    MERCHANT("MERCHANT"),

    SUPPORT("SUPPORT");

    private final String authority;

    @Override
    public String toString() {
        return this.getAuthority();
    }

    public static Optional<Authority> parse(String stringAuthority) {
        String preparedStrAuthority = stringAuthority.toUpperCase().trim();

        if (preparedStrAuthority.equals(MERCHANT.getAuthority()) || preparedStrAuthority.equals(SUPPORT.getAuthority())) {
            return Optional.of(Authority.valueOf(preparedStrAuthority));
        }

        return Optional.empty();
    }

}

