package com.basak.dalcom.domain.accounts.service.exceptions;

import com.basak.dalcom.domain.common.service.exceptions.DuplicatedFieldException;

public class DuplicatedEmailException extends DuplicatedFieldException {

    public DuplicatedEmailException() {
        super("email");
    }
}
