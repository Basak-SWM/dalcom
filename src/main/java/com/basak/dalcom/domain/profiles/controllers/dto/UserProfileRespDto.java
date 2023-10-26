package com.basak.dalcom.domain.profiles.controllers.dto;

import com.basak.dalcom.domain.profiles.data.UserProfile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfileRespDto {

    private Boolean voiceUsageAgreement;

    public UserProfileRespDto(UserProfile userProfile) {
        this.voiceUsageAgreement = userProfile.isVoiceUsageAgreement();
    }
}
