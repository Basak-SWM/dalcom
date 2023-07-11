package com.basak.dalcom.domain.accounts;

import java.util.Arrays;
import java.util.Optional;
import lombok.Getter;

public enum AccountRole {
    USER("1"), COACH("2");

    @Getter
    private final String value;

    AccountRole(String value) {
        this.value = value;
    }

    public static Optional<AccountRole> enumOf(String s) {
        return Arrays.stream(AccountRole.values()).filter(r -> r.value.equals(s)).findAny();
    }

    public static Optional<String> valueOf(AccountRole role) {
        return Arrays.stream(AccountRole.values()).filter(r -> r.equals(role)).map(r -> r.value)
            .findAny();
    }
}