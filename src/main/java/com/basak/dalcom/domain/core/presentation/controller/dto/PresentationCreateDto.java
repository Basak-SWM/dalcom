package com.basak.dalcom.domain.core.presentation.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class PresentationCreateDto {

    @Schema(description = "생성할 프레젠테이션의 정보", type = "PresentationDto")
    private PresentationDto presentation;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PresentationDto {

        @Schema(description = "제목", type = "string", example = "중간 시험 대체 발표")
        protected String title;
        @Schema(description = "개요", type = "string", example = "정렬 알고리즘 각각의 시간 복잡도 및 공간 복잡도 분석")
        protected String outline;
        @Schema(description = "잘 하고 싶은 점", type = "string", example = "각각의 장단점이 잘 부각되도록, 구체적인 예시와 함께 설명하기")
        protected String checkpoint;
    }
}
