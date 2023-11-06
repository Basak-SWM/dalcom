package com.basak.dalcom.domain.common.exception.stereotypes;

import com.basak.dalcom.domain.common.exception.HandledException;
import org.springframework.http.HttpStatus;

public class UnauthorizedException extends HandledException {

    public UnauthorizedException(String requestedOperation, String requiredPrivilege) {
        super(HttpStatus.UNAUTHORIZED,
            "You are not authorized to '" + requestedOperation + "'. You need '" + requiredPrivilege
                + "' privilege.");
    }
}
