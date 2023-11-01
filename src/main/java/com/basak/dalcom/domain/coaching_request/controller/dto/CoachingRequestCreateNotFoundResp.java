package com.basak.dalcom.domain.coaching_request.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CoachingRequestCreateNotFoundResp {

    @Schema(description = "찾지 못한 자원", type = "string", example = "SPEECH")
    private final DetailCode detailCode;

    public enum DetailCode {
        COACH, SPEECH
    }
}
