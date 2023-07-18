package com.basak.dalcom.domain.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public class HandledException extends RuntimeException {

    private final HttpStatus status;
    private final String reason;
}
