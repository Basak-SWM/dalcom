package com.basak.dalcom.domain.core.presentation.controller.response_dto;

import com.basak.dalcom.domain.core.presentation.data.Presentation;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class PresentationCreateSuccessDto {

    @Schema(description = "생성된 presentaion의 id")
    private final Integer id;

    public PresentationCreateSuccessDto(Presentation presentation) {
        id = presentation.getId();
    }
}
