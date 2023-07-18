package com.basak.dalcom.domain.common.controllers.exceptions;

import com.basak.dalcom.domain.common.exception.ErrorStatusWrapper;

public abstract class UnhandledException extends ControllerException {

    UnhandledException(ErrorStatusWrapper errorStatusWrapper,
        String message) {
        super(errorStatusWrapper, message);
    }
}
