package com.basak.dalcom.domain.core.presentation.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class PresentationCreateDto {

    @Schema(description = "소유자 account uuid", type = "string", example = "da7fe365-8774-4355-e6f2-629cc62e09e8")
    @NotBlank(message = "필수 입력 값입니다.")
    private String accountUuid;

    @Schema(description = "생성할 프레젠테이션의 정보", type = "PresentationDto")
    private NoIdPresentationDto presentation;
}
