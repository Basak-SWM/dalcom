package com.basak.dalcom.domain.common.exception.stereotypes;

import com.basak.dalcom.domain.common.exception.HandledException;
import org.springframework.http.HttpStatus;

public class ConflictException extends HandledException {

    public ConflictException(String reason) {
        super(HttpStatus.CONFLICT, "Conflict : " + reason);
    }
}
