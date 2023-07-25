package com.basak.dalcom.domain.core.analysis_record.data;

import java.util.Arrays;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
public enum AnalysisRecordType {
    STT("STT"),
    DECIBEL("DECIBEL"),
    HERTZ("HERTZ"),
    WPM("WPM"),
    PAUSE("PAUSE");

    @Getter
    private final String value;

    public static Optional<AnalysisRecordType> enumOf(String s) {
        return Arrays.stream(AnalysisRecordType.values())
            .filter(r -> r.value.equals(s))
            .findAny();
    }

    public static Optional<String> valueOf(AnalysisRecordType recordType) {
        return Arrays.stream(AnalysisRecordType.values())
            .filter(r -> r.equals(recordType))
            .map(r -> r.value)
            .findAny();
    }
}
