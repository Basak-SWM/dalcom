package com.basak.dalcom.domain.core.presentation.controller.request_dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PresentationCreateDto {

    @Schema(description = "소유자 account uuid", type = "string", example = "da7fe365-8774-4355-e6f2-629cc62e09e8")
    private String accountUuid;

    @Schema(description = "제목", type = "string", example = "철학의 물음들 중간 대체 발표")
    private String title;

    @Schema(description = "개요", type = "string", example = "파스칼 내기 논증의 한계와 가능한 대안에 대한 논증")
    private String outline;

    @Schema(description = "잘 하고 싶은 부분", type = "string", example = "논리를 뒷받침하는 근거를 차례로 나열하며 주장을 전개하기")
    private String checkpoint;
}
