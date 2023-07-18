package com.basak.dalcom.domain.common.exception;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public class HandledException extends RuntimeException {

    private final HttpStatus status;
    private final String reason;
}
