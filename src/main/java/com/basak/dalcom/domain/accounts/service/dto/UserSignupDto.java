package com.basak.dalcom.domain.accounts.service.dto;

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
}
