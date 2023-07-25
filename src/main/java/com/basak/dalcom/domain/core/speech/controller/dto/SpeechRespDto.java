package com.basak.dalcom.domain.core.speech.controller.dto;

import com.basak.dalcom.domain.core.speech.data.Speech;
import io.swagger.v3.oas.annotations.media.Schema;
import java.net.URL;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class SpeechRespDto {

    @Schema(description = "사용자 기호 Raw 저장값")
    private final String userSymbol;
    @Schema(description = "생성된 speech의 id")
    private Integer id;
    @Schema(description = "정렬된 AudioSegment Presigned URL 목록")
    private List<URL> audioSegments;
    @Schema(description = "녹음 종료 여부")
    private Boolean recordDone;

    public SpeechRespDto(Speech speech) {
        this.id = speech.getId();
        this.userSymbol = speech.getUserSymbol();
        this.recordDone = speech.getRecordDone();
        if (speech.getAudioSegments() != null) {
            this.audioSegments = speech.getAudioSegments().stream()
                .map(audioSegment -> audioSegment.getFullAudioS3Url())
                .map(url -> {
                    try {
                        return new URL(url);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }).toList();
        }
    }
}
