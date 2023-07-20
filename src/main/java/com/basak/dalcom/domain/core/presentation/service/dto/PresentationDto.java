package com.basak.dalcom.domain.core.presentation.service.dto;

import com.basak.dalcom.domain.core.presentation.controller.dto.PresentationCreateDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PresentationDto {

    private Integer id;
    private String title;
    private String outline;
    private String checkpoint;

    public PresentationDto(PresentationCreateDto.PresentationDto dto) {
        this.title = dto.getTitle();
        this.outline = dto.getOutline();
        this.checkpoint = dto.getCheckpoint();
    }
}
