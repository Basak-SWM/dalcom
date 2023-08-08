package com.basak.dalcom.domain.core.speech.controller.dto;

import com.basak.dalcom.domain.core.speech.data.AIChatLog;
import lombok.Getter;

@Getter
public class AIChatLogCreateResDto {

    private final Long id;

    public AIChatLogCreateResDto(AIChatLog log) {
        this.id = log.getId();
    }
}
