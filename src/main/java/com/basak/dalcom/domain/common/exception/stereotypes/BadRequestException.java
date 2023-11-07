package com.basak.dalcom.domain.common.exception.stereotypes;

import com.basak.dalcom.domain.common.exception.HandledException;
import org.springframework.http.HttpStatus;

public class BadRequestException extends HandledException {

    public BadRequestException(String reason) {
        super(HttpStatus.BAD_REQUEST, "Bad Request : " + reason);
    }
}
