package com.basak.dalcom.domain.accounts.controller.response_dto;

import com.basak.dalcom.domain.accounts.data.Account;
import lombok.Getter;

@Getter
public class SignupSuccessDto {

    private final String uuid;

    public SignupSuccessDto(Account account) {
        uuid = account.getUuid();
    }
}
