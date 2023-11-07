package com.basak.dalcom.domain.coaching_request.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CoachingRequestUpdateReq {

    @Schema(description = "코치 메시지", type = "string", example = "맨 첫줄의 임팩트가 부족하네요.")
    private String coachMessage;
    @Schema(description = "사용자 기호", type = "json string", example = "(stringified json)")
    private String jsonUserSymbol;
}
