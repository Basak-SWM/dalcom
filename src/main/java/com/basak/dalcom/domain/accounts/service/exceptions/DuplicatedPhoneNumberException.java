package com.basak.dalcom.domain.accounts.service.exceptions;

import com.basak.dalcom.domain.common.exception.custom.DuplicatedFieldException;

@Deprecated
public class DuplicatedPhoneNumberException extends DuplicatedFieldException {

    public DuplicatedPhoneNumberException() {
        super("phoneNumber");
    }
}
