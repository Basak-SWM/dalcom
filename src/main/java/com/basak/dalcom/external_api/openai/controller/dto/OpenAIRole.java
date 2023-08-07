package com.basak.dalcom.external_api.openai.controller.dto;

import java.util.Arrays;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum OpenAIRole {
    USER("user"),
    SYSTEM("system"),
    ASSISTANT("assistant");

    @Getter
    private final String value;

    public static Optional<OpenAIRole> enumOf(String s) {
        return Arrays.stream(OpenAIRole.values())
            .filter(r -> r.value.equals(s))
            .findAny();
    }

    public static Optional<String> valueOf(OpenAIRole recordType) {
        return Arrays.stream(OpenAIRole.values())
            .filter(r -> r.equals(recordType))
            .map(r -> r.value)
            .findAny();
    }

}
