package com.basak.dalcom.domain.core.speech.controller.dto;

import com.basak.dalcom.domain.core.speech.data.Speech;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class SpeechRecordDoneSuccessDto {

    @Schema(description = "생성된 speech의 id")
    private final Integer id;

    public SpeechRecordDoneSuccessDto(Speech speech) {
        id = speech.getId();
    }
}
