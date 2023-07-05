package com.basak.dalcom.domain.accounts;

import java.util.Arrays;
import java.util.Optional;

public enum AccountRole {
    USER(1),
    COACH(2);

    private final int value;

    AccountRole(int value) {
        this.value = value;
    }

    public static Optional<AccountRole> enumOf(Integer n) {
        return Arrays.stream(AccountRole.values())
                .filter(r -> r.value == n)
                .findAny();
    }

    public static Optional<Integer> valueOf(AccountRole role) {
        return Arrays.stream(AccountRole.values())
                .filter(r -> r.equals(role))
                .map(r -> r.value)
                .findAny();
    }
}
