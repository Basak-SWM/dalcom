package com.basak.dalcom.domain.common.controllers.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class FieldConflictResponseException extends ResponseStatusException {

    public FieldConflictResponseException(String fieldDescription) {
        super(HttpStatus.CONFLICT, "Given value is already used : " + fieldDescription);
    }
}
