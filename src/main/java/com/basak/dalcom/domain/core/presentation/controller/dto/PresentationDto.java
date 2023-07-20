package com.basak.dalcom.domain.core.presentation.controller.dto;

import com.basak.dalcom.domain.core.presentation.data.Presentation;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PresentationDto {

    @Schema(description = "제목", type = "string", example = "중간 시험 대체 발표")
    protected String title;
    @Schema(description = "개요", type = "string", example = "정렬 알고리즘 각각의 시간 복잡도 및 공간 복잡도 분석")
    protected String outline;
    @Schema(description = "잘 하고 싶은 점", type = "string", example = "각각의 장단점이 잘 부각되도록, 구체적인 예시와 함께 설명하기")
    protected String checkpoint;
    @Schema(description = "id", type = "integer", example = "1")
    protected Integer id;

    public PresentationDto(Presentation presentation) {
        this.id = presentation.getId();
        this.title = presentation.getTitle();
        this.outline = presentation.getOutline();
        this.checkpoint = presentation.getCheckpoint();
    }

    public void setIdNull() {
        this.id = null;
    }
}
