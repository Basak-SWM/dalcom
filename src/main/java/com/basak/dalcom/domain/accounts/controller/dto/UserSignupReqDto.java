package com.basak.dalcom.domain.accounts.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UserSignupReqDto extends SignupReqDto {

    @Schema(description = "음성 데이터 활용 동의 여부", type = "boolean", example = "true")
    @NotNull(message = "음성 데이터 활용 동의 여부는 필수 입력 값입니다.")
    private Boolean voiceUsageAgreement;
}