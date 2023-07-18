package com.basak.dalcom.domain.common.exception;

import com.basak.dalcom.domain.common.controllers.exceptions.ControllerException;
import com.basak.dalcom.domain.common.controllers.exceptions.HandledException;
import com.basak.dalcom.domain.common.controllers.exceptions.UnhandledException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {HandledException.class})
    public ResponseEntity<? extends ErrorResponseDto> handleHandledException(HandledException ex) {
        return handleException(ex);
    }

    @ExceptionHandler(value = {UnhandledException.class})
    public ResponseEntity<? extends ErrorResponseDto> handleHandledException(
        UnhandledException ex) {
        log.error("Unhandled Exception occurred : " + ex);
        return handleException(ex);
    }

    private ResponseEntity<? extends ErrorResponseDto> handleException(ControllerException ex) {
        return ex.toResponseEntity();
    }
}
