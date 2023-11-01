package com.basak.dalcom.domain.accounts.service.dto;

import com.basak.dalcom.domain.accounts.controller.dto.CoachSignupReqDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class CoachSignupDto {

    private String username;
    private String nickname;

    private String email;

    private String phoneNumber;

    private String password;

    private String shortIntroduce;

    private String speciality;

    private String introduce;

    private String youtubeUrl;

    public CoachSignupDto(CoachSignupReqDto dto) {
        this.username = dto.getUsername();
        this.nickname = dto.getNickname();
        this.email = dto.getEmail();
        this.phoneNumber = dto.getPhoneNumber();
        this.password = dto.getPassword();
        this.shortIntroduce = dto.getShortIntroduce();
        this.speciality = dto.getSpeciality();
        this.introduce = dto.getIntroduce();
        this.youtubeUrl = dto.getYoutubeUrl();
    }
}
