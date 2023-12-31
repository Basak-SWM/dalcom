package com.basak.dalcom.domain.core.analysis_result.data;

import java.util.Arrays;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
public enum AnalysisType {
    STT("STT"),
    DECIBEL("DECIBEL"),
    HERTZ("HERTZ"),
    HERTZ_AVG("HERTZ_AVG"),
    LPM("LPM"),
    LPM_AVG("LPM_AVG"),
    PAUSE("PAUSE"),
    PAUSE_RATIO("PAUSE_RATIO"),
    SPEECH_CORRECTION("SPEECH_CORRECTION");

    @Getter
    private final String value;

    public static Optional<AnalysisType> enumOf(String s) {
        return Arrays.stream(AnalysisType.values())
            .filter(r -> r.value.equals(s))
            .findAny();
    }

    public static Optional<String> valueOf(AnalysisType recordType) {
        return Arrays.stream(AnalysisType.values())
            .filter(r -> r.equals(recordType))
            .map(r -> r.value)
            .findAny();
    }
}
