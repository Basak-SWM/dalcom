package com.basak.dalcom.domain.core.presentation.controller.dto;

import com.basak.dalcom.domain.core.presentation.data.Presentation;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class NoIdPresentationDto extends PresentationDto {

    @Schema(hidden = true)
    private Integer id;

    public NoIdPresentationDto(Presentation presentation) {
        super(presentation);
        setIdNull();
    }
}