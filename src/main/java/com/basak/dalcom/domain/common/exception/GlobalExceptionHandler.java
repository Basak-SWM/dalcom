package com.basak.dalcom.domain.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {Exception.class})
    public void unhandledException(Exception ex) {

    }

    @ExceptionHandler(value = {HandledException.class})
    public void handledException(HandledException ex) {

    }
}
