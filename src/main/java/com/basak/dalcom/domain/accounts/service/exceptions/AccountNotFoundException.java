package com.basak.dalcom.domain.accounts.service.exceptions;

import com.basak.dalcom.domain.common.service.exceptions.ObjectNotFoundException;

public class AccountNotFoundException extends ObjectNotFoundException {

    public AccountNotFoundException(String searchCondition) {
        super("account", searchCondition);
    }
}
