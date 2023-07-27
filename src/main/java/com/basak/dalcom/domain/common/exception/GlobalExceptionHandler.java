package com.basak.dalcom.domain.common.exception;

import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<?> unhandledException(Exception ex) {
        for (StackTraceElement el : ex.getStackTrace()) {
            log.error(el.toString());
        }
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {HandledException.class})
    public ResponseEntity<?> handledException(HandledException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("message", ex.getReason());

        for (StackTraceElement el : ex.getStackTrace()) {
            log.info(el.toString());
        }

        return new ResponseEntity<>(body, ex.getStatus());
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<?> handleControllerValidationException(
        MethodArgumentNotValidException ex) {
        return new ResponseEntity<>(ex.getBindingResult().getAllErrors(), HttpStatus.BAD_REQUEST);
    }
}
