package com.basak.dalcom.domain.core.speech.controller.dto;

import com.basak.dalcom.domain.core.speech.data.Speech;
import io.swagger.v3.oas.annotations.media.Schema;
import java.net.URL;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class SpeechRespDto {

    private static DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    @Schema(description = "사용자 기호 Raw 저장값")
    private final String userSymbol;
    @Schema(description = "생성된 speech의 id")
    private Integer id;
    @Schema(description = "정렬된 AudioSegment Presigned URL 목록")
    private List<URL> audioSegments = new ArrayList<>();
    @Schema(description = "녹음 종료 여부")
    private Boolean recordDone;
    @Schema(description = "참조 speech의 id")
    private Integer refSpeechId = null;
    @Schema(description = "Full audio가 저장된 경로 (presigned)")
    private String fullAudioS3Url;
    @Schema(description = "생성 일시 (UTC)", example = "2023-08-07T04:06:56Z")
    private String createdDate;
    @Schema(description = "수정 일시 (UTC)", example = "2023-08-07T04:06:56Z")
    private String lastModifiedDate;
    @Schema(description = "프레젠테이션 내에서 speech의 순서", example = "1")
    private Integer order;
    @Schema(description = "북마크 여부")
    private Boolean bookmarked;

    @Schema(description = "피드백 개수")
    private Integer feedbackCount;
    @Schema(description = "LPM (속도) 평균")
    private Float avgLPM;
    @Schema(description = "휴지 비율")
    private Float pauseRatio;
    @Schema(description = "F0(목소리 톤) 평균")
    private Float avgF0;


    public SpeechRespDto(Speech speech) {
        this.id = speech.getId();
        this.userSymbol = speech.getUserSymbol();
        this.recordDone = speech.getRecordDone();
        this.fullAudioS3Url = speech.getFullAudioS3Url();
        this.createdDate = speech.getCreatedDate().atOffset(ZoneOffset.UTC).format(FORMATTER);
        this.lastModifiedDate = speech.getLastModifiedDate().atOffset(ZoneOffset.UTC)
            .format(FORMATTER);
        this.order = speech.getOrder();
        this.bookmarked = speech.getBookmarked();
        this.feedbackCount = speech.getFeedbackCount();
            this.avgLPM = speech.getAvgLPM();
        this.pauseRatio = speech.getPauseRatio();
            this.avgF0 = speech.getAvgF0();

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

        if (speech.getReferenceSpeech() != null) {
            this.refSpeechId = speech.getReferenceSpeech().getId();
        }
    }
}
