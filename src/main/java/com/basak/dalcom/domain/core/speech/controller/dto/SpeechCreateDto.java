package com.basak.dalcom.domain.core.speech.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class SpeechCreateDto {

    @Schema(description = "참고할 스피치의 ID", type = "integer")
    private Integer referenceSpeechId;
}
