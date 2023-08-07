package com.basak.dalcom.domain.core.presentation.controller.dto;

import com.basak.dalcom.domain.core.presentation.data.Presentation;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.format.DateTimeFormatter;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PresentationRespDto {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_DATE_TIME;


    @Schema(description = "id", type = "integer", example = "1")
    private final Integer id;
    @Schema(description = "제목", type = "string", example = "중간 시험 대체 발표")
    private final String title;
    @Schema(description = "개요", type = "string", example = "정렬 알고리즘 각각의 시간 복잡도 및 공간 복잡도 분석")
    private final String outline;
    @Schema(description = "잘 하고 싶은 점", type = "string", example = "각각의 장단점이 잘 부각되도록, 구체적인 예시와 함께 설명하기")
    private final String checkpoint;
    @Schema(description = "생성 일시", example = "2023-08-02 18:33:04")
    private final String createdDate;
    @Schema(description = "수정 일시", example = "2023-08-02 18:33:04")
    private String lastModifiedDate;

    public PresentationRespDto(Presentation presentation) {
        this.id = presentation.getId();
        this.title = presentation.getTitle();
        this.outline = presentation.getOutline();
        this.checkpoint = presentation.getCheckpoint();
        this.createdDate = presentation.getCreatedDate().format(FORMATTER);
        this.lastModifiedDate = presentation.getLastModifiedDate().format(FORMATTER);
    }
}