package com.basak.dalcom.domain.coaching_request.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CoachingRequestCreateReq {

    @Schema(description = "대상 스피치 ID", type = "integer", example = "1")
    @NotNull(message = "대상 스피치 ID는 필수 입력 값입니다.")
    private Integer speechId;

    @Schema(description = "요청 코치 uuid", type = "UUID", example = "123e4567-e89b-12d3-a456-426614174000")
    @NotNull(message = "요청 코치 uuid는 필수 입력 값입니다.")
    private UUID coachUuid;

    @Schema(description = "코칭 요청 메시지", type = "string", example = "코칭 요청 메시지")
    @NotNull(message = "코칭 요청 메시지는 필수 입력 값입니다.")
    private String userMessage;
}
