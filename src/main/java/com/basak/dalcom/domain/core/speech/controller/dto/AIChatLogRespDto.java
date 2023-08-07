package com.basak.dalcom.domain.core.speech.controller.dto;

import com.basak.dalcom.domain.core.speech.data.AIChatLog;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import lombok.Getter;

@Getter
public class AIChatLogRespDto {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    @Schema(description = "AIChatLog의 id", type = "integer", example = "1")
    private final Long id;

    @Schema(description = "질문", type = "string", example = "안녕하세요")
    private final String prompt;

    @Schema(description = "응답", type = "string", example = "안녕하세요")
    private final Optional<String> result;

    @Schema(description = "응답이 완료되었는지 여부", type = "boolean", example = "true")
    private final Boolean isDone;

    @Schema(description = "요청 일시 (UTC)", type = "datetime", example = "2023-08-07T04:06:56Z")
    private final String createdDate;

    @Schema(description = "수정 일시 (UTC)", example = "2023-08-07T04:06:56Z")
    private final String lastModifiedDate;


    public AIChatLogRespDto(AIChatLog log) {
        this.id = log.getId();
        this.prompt = log.getPrompt();
        this.result = Optional.ofNullable(log.getResult());
        this.isDone = log.getIsDone();
        this.createdDate = log.getCreatedDate().atOffset(ZoneOffset.UTC).format(FORMATTER);
        this.lastModifiedDate = log.getLastModifiedDate().atOffset(ZoneOffset.UTC)
            .format(FORMATTER);
    }
}
