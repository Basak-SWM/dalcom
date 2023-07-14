package com.basak.dalcom.domain.accounts.controller.response_dto;

import com.basak.dalcom.domain.accounts.data.Account;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class SignupSuccessDto {

    @Schema(description = "생성된 account의 uuid")
    private final String uuid;

    public SignupSuccessDto(Account account) {
        uuid = account.getUuid();
    }
}
