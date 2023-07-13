package com.basak.dalcom.domain.accounts.service.exceptions;

import com.basak.dalcom.domain.common.exception.custom.DuplicatedFieldException;

@Deprecated
public class DuplicatedEmailException extends DuplicatedFieldException {

    public DuplicatedEmailException() {
        super("email");
    }
}
