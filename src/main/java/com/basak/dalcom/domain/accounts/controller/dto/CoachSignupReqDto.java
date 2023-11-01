package com.basak.dalcom.domain.accounts.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CoachSignupReqDto extends SignupReqDto {

    @Schema(description = "한 줄 소개", type = "string", example = "취업하고 싶은 코치 박유빈입니다.")
    private String shortIntroduce;

    @Schema(description = "전문 분야", type = "string", example = "과제 발표")
    private String speciality;

    @Schema(description = "소개", type = "string", example = "저는 1997년 10월 1일 인천에서 태어나..")
    private String introduce;

    @Schema(description = "소개 유튜브 url", type = "string", example = "https://www.youtube.com/watch?v=lyeLYUCalNw")
    private String youtubeUrl;
}
