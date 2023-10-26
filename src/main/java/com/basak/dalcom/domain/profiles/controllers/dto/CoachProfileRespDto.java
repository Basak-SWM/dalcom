package com.basak.dalcom.domain.profiles.controllers.dto;

import com.basak.dalcom.domain.profiles.data.CoachProfile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CoachProfileRespDto {

    private String shortIntroduce;

    private String speciality;

    private String introduce;

    private String youtubeUrl;


    public CoachProfileRespDto(CoachProfile coachProfile) {
        this.shortIntroduce = coachProfile.getShortIntroduce();
        this.speciality = coachProfile.getSpeciality();
        this.introduce = coachProfile.getIntroduce();
        this.youtubeUrl = coachProfile.getYoutubeUrl();
    }
}
