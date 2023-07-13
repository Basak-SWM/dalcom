package com.basak.dalcom.domain.common.controllers.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class NotFoundResponseException extends ResponseStatusException {

    public NotFoundResponseException(String reason) {
        super(HttpStatus.NOT_FOUND, reason);
    }
}
