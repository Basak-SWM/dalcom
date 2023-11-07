package com.basak.dalcom.domain.profiles.controllers.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CoachProfileUpdateReq {

    @Schema(description = "짧은 소개", example = "안녕하세요. 코치입니다.")
    private String shortIntroduce;
    @Schema(description = "전문분야", example = "대학 PT")
    private String speciality;
    @Schema(description = "소개", example = "안녕하세요. 코치입니다. 저는 인천에서 태어나,,")
    private String introduce;
    @Schema(description = "유튜브 URL", example = "https://www.youtube.com/watch?v=1qJGpI5qJWU")
    private String youtubeUrl;
}
