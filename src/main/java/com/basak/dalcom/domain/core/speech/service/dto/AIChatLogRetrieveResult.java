package com.basak.dalcom.domain.core.speech.service.dto;

import com.basak.dalcom.domain.core.speech.data.AIChatLog;
import java.util.List;
import lombok.Getter;

@Getter
public class AIChatLogRetrieveResult {

    private final List<AIChatLog> completedLogs;
    private final List<AIChatLog> uncompletedLogs;

    public AIChatLogRetrieveResult(List<AIChatLog> logs) {
        this.completedLogs = logs.stream()
            .filter(log -> log.getIsDone())
            .toList();
        this.uncompletedLogs = logs.stream()
            .filter(log -> !log.getIsDone())
            .toList();
    }
}
