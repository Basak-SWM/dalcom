package com.basak.dalcom.domain.core.speech.controller.dto;

import com.basak.dalcom.domain.core.speech.data.SttResult;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class SttResultRespDto {

    @Schema(description = "STT 결과의 Id")
    private final Integer id;

    @Schema(description = "저장 일시")
    private final LocalDateTime createdDate;

    @Schema(description = "STT 결과")
    private final String body;

    public SttResultRespDto(SttResult sttResult) {
        this.id = sttResult.getId();
        this.createdDate = sttResult.getCreatedDate();
        this.body = sttResult.getBody();
    }
}
