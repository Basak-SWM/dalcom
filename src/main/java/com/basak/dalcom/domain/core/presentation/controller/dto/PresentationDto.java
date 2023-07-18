package com.basak.dalcom.domain.core.presentation.controller.dto;

import com.basak.dalcom.domain.core.presentation.data.Presentation;
import lombok.Getter;

@Getter
public class PresentationDto {

    private final Integer id;

    private final String title;

    private final String outline;

    private final String checkpoint;

    public PresentationDto(Presentation presentation) {
        this.id = presentation.getId();
        this.title = presentation.getTitle();
        this.outline = presentation.getOutline();
        this.checkpoint = presentation.getCheckpoint();
    }
}
