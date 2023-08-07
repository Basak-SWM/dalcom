package com.basak.dalcom.domain.core.speech.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class AIChatLogListRespDto {

    @Schema(description = "응답이 완료된 AIChatLog 목록")
    private List<AIChatLogRespDto> completedChatLogs;

    @Schema(description = "응답이 완료되지 않은 AIChatLog 목록")
    private List<AIChatLogRespDto> uncompletedChatLogs;
}
