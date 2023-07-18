package com.basak.dalcom.domain.common.controllers.exceptions;

import com.basak.dalcom.domain.common.exception.ErrorStatusWrapper;

public abstract class HandledException extends ControllerException {

    HandledException(ErrorStatusWrapper errorStatusWrapper,
        String message) {
        super(errorStatusWrapper, message);
    }
}
