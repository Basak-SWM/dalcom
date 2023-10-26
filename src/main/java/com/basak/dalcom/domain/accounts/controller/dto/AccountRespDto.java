package com.basak.dalcom.domain.accounts.controller.dto;

import com.basak.dalcom.domain.accounts.data.Account;
import com.basak.dalcom.domain.accounts.data.AccountRole;
import com.basak.dalcom.domain.profiles.controllers.dto.CoachProfileRespDto;
import com.basak.dalcom.domain.profiles.controllers.dto.UserProfileRespDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class AccountRespDto {

    @Schema(description = "사용자 uuid", type = "string", example = "27f7e5ff-0638-4861-ab4c-b74fff0b7496")
    private final String uuid;

    @Schema(description = "닉네임", type = "string", example = "박유빈")
    private final String nickname;

    @Schema(description = "이메일", type = "string", example = "tokpeanutUser@gmail.com")
    private final String email;

    @Schema(description = "전화번호", type = "string", example = "010-1234-5678")
    private final String phoneNumber;

    @Schema(description = "사용자 정보", implementation = UserProfileRespDto.class, nullable = true)
    private UserProfileRespDto userProfile = null;

    @Schema(description = "코치 정보", implementation = CoachProfileRespDto.class, nullable = true)
    private CoachProfileRespDto coachProfile = null;

    public AccountRespDto(Account account) {
        this.uuid = account.getUuid();
        this.nickname = account.getNickname();
        this.email = account.getEmail();
        this.phoneNumber = account.getPhoneNumber();
        if ((account.getRole() == AccountRole.USER) && (account.getUserProfile() != null)) {
            this.userProfile = new UserProfileRespDto(account.getUserProfile());
        }
        if ((account.getRole() == AccountRole.COACH) && (account.getCoachProfile() != null)) {
            this.coachProfile = new CoachProfileRespDto(account.getCoachProfile());
        }
    }
}
