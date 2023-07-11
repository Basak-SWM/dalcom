package com.basak.dalcom.domain.accounts.controller.exceptions;

import com.basak.dalcom.domain.common.controllers.exceptions.FieldConflictResponseException;

/**
 * 이미 사용중인 값으로 Account에 대한 생성, 또는 갱신이 요청된 경우 발생하는 예외 (409)
 */
public class DuplicatedAccountFieldException extends FieldConflictResponseException {

    public DuplicatedAccountFieldException(String field) {
        super(field);
    }
}
