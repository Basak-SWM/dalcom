package com.basak.dalcom.domain.core.speech.service.dto;

import lombok.Getter;
import org.openapitools.jackson.nullable.JsonNullable;

@Getter
public class SpeechUpdateDto {

    private final Integer presentationId;
    private final Integer speechId;
    private final JsonNullable<String> userSymbol;
    private final JsonNullable<Boolean> bookmarked;

    public SpeechUpdateDto(Integer presentationId, Integer speechId,
        JsonNullable<String> userSymbol, JsonNullable<Boolean> bookmarked) {
        this.presentationId = presentationId;
        this.speechId = speechId;
        this.userSymbol = userSymbol;
        this.bookmarked = bookmarked;
    }
}
