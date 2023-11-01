package com.basak.dalcom.domain.coaching_request.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CoachingRequestDenialReq {

    @Schema(description = "거절 사유", type = "string", example = "구려요")
    private final String reason;
}
