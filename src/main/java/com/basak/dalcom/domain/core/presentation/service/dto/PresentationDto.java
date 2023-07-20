package com.basak.dalcom.domain.core.presentation.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PresentationDto {

    private Integer id;
    private String title;
    private String outline;
    private String checkpoint;
}
