package com.basak.dalcom.domain.profiles.controllers.dto;

import com.basak.dalcom.domain.profiles.data.CoachProfile;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PublicCoachProfileRespDto {

    @Schema(description = "짧은 소개", example = "안녕하세요. 코치입니다.")
    protected String shortIntroduce;
    @Schema(description = "전문분야", example = "대학 PT")
    protected String speciality;
    @Schema(description = "소개", example = "안녕하세요. 코치입니다. 저는 인천에서 태어나,,")
    protected String introduce;
    @Schema(description = "유튜브 URL", example = "https://www.youtube.com/watch?v=1qJGpI5qJWU")
    protected String youtubeUrl;
    @Schema(description = "코칭 수락 횟수", example = "0")
    protected Integer acceptCount;

    @Schema(description = "코치 account UUID", type = "UUID", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID uuid;
    @Schema(description = "코치 ID", type = "string", example = "qkrdbqls1002")
    private String username;
    @Schema(description = "코치 닉네임", type = "string", example = "불꽃코치")
    private String nickname;

    public static PublicCoachProfileRespDto fromEntity(CoachProfile coachProfile) {
        return PublicCoachProfileRespDto.builder()
            .uuid(UUID.fromString(coachProfile.getAccount().getUuid()))
            .username(coachProfile.getAccount().getUsername())
            .nickname(coachProfile.getAccount().getNickname())
            .shortIntroduce(coachProfile.getShortIntroduce())
            .speciality(coachProfile.getSpeciality())
            .introduce(coachProfile.getIntroduce())
            .youtubeUrl(coachProfile.getYoutubeUrl())
            .acceptCount(coachProfile.getAcceptCount())
            .build();
    }
}
