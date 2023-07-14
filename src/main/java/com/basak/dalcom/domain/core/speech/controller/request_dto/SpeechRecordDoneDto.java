package com.basak.dalcom.domain.core.speech.controller.request_dto;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class SpeechRecordDoneDto {
    @Schema(description = "프리젠테이션 id", type = "integer")
    @NotBlank
    private Integer presentationId;
}
