package com.basak.dalcom.domain.core.speech.service.dto;

import com.basak.dalcom.domain.core.speech.data.AIChatLog;
import com.basak.dalcom.external_api.openai.controller.dto.OpenAIRole;
import java.util.List;
import lombok.Getter;

@Getter
public class AIChatLogRetrieveResult {

    private final List<AIChatLog> completedLogs;
    private final List<AIChatLog> uncompletedLogs;
    private final List<AIChatLog> systemLogs;

    public AIChatLogRetrieveResult(List<AIChatLog> logs) {
        this.completedLogs = logs.stream()
            .filter(log -> log.getIsDone() && log.getRole().equals(OpenAIRole.USER))
            .toList();
        this.uncompletedLogs = logs.stream()
            .filter(log -> !log.getIsDone() && log.getRole().equals(OpenAIRole.USER))
            .toList();
        this.systemLogs = logs
            .stream().filter(log -> log.getRole().equals(OpenAIRole.SYSTEM))
            .toList();
    }
}
