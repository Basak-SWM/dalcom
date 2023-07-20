package com.basak.dalcom.domain.core.audio_segment.controller.dto;

import com.basak.dalcom.domain.core.audio_segment.data.AudioSegment;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class AudioSegmentRespDto {

    @Schema(description = "AudioSegment의 id")
    private Long id;

    @Schema(description = "AudioSegment가 실제로 저장된 위치")
    private String fullAudioS3Url;

    @Schema(description = "생성 일시")
    private LocalDateTime createdDate;

    public AudioSegmentRespDto(AudioSegment audioSegment) {
        this.id = audioSegment.getId();
        this.fullAudioS3Url = audioSegment.getFullAudioS3Url();
        this.createdDate = audioSegment.getCreatedDate();
    }
}
