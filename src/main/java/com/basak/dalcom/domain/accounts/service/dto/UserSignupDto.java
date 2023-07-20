package com.basak.dalcom.domain.accounts.service.dto;

import com.basak.dalcom.domain.accounts.controller.dto.UserSignupReqDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class UserSignupDto {

    private String nickname;

    private String email;

    private String phoneNumber;

    private String password;

    private Boolean voiceUsageAgreement;

    public UserSignupDto(UserSignupReqDto dto) {
        this.nickname = dto.getNickname();
        this.email = dto.getEmail();
        this.phoneNumber = dto.getPhoneNumber();
        this.password = dto.getPassword();
        this.voiceUsageAgreement = dto.getVoiceUsageAgreement();
    }
}
