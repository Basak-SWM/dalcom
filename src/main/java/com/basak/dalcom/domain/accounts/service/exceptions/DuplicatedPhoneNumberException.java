package com.basak.dalcom.domain.accounts.service.exceptions;

import com.basak.dalcom.domain.common.service.exceptions.DuplicatedFieldException;

public class DuplicatedPhoneNumberException extends DuplicatedFieldException {

    public DuplicatedPhoneNumberException() {
        super("phoneNumber");
    }
}
