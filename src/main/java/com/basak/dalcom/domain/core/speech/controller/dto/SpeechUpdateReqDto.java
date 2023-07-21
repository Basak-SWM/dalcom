package com.basak.dalcom.domain.core.speech.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.openapitools.jackson.nullable.JsonNullable;

@Getter
public class SpeechUpdateReqDto {

    @Schema(description = "사용자 기호", type = "stringfied json")
    private final JsonNullable<String> userSymbol = JsonNullable.undefined();
}
