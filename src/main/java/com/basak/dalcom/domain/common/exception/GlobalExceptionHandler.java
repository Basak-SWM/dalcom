package com.basak.dalcom.domain.common.exception;

import io.sentry.Sentry;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@AllArgsConstructor
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    private final Environment environment;

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<?> unhandledException(Exception ex) {
        catchAsSentry(ex);
        for (StackTraceElement el : ex.getStackTrace()) {
            log.error(el.toString());
        }
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {HandledException.class})
    public ResponseEntity<?> handledException(HandledException ex) {
        catchAsSentry(ex);
        Map<String, Object> body = new HashMap<>();
        body.put("message", ex.getReason());

        for (StackTraceElement el : ex.getStackTrace()) {
            log.info(el.toString());
        }

        return new ResponseEntity<>(body, ex.getStatus());
    }

    private void catchAsSentry(Exception ex) {
        if (Arrays.asList(environment.getActiveProfiles()).contains("staging")) {
            Sentry.captureException(ex);
        }
    }
}
