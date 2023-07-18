package com.basak.dalcom.domain.core.speech.controller.dto;

import com.basak.dalcom.domain.core.speech.data.Speech;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class GetSpeechDto {

    @Schema(description = "생성된 speech의 id")
    private final String fullAudioS3Uri;

    @Schema(description = "STT 결과 (Clova 응답 Raw 저장값)")
    private final String sttResult;

    @Schema(description = "사용자 기호 Raw 저장값")
    private final String userSymbol;

    public GetSpeechDto(Speech speech) {
        this.fullAudioS3Uri = speech.getFullAudioS3Url();
        this.sttResult = speech.getSttScript();
        this.userSymbol = speech.getUserSymbol();
    }
}
