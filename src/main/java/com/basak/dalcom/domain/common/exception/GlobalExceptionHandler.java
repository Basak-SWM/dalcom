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
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@AllArgsConstructor
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    private final Environment environment;

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<?> unexpectedException(Exception ex) {
        catchAsSentry(ex);
        log.error("Unexpected exception: {}" + ex.toString());
        printStackTraceIfAllowed(ex);

        Map<String, Object> body = new HashMap<>();
        String message = isClientResponseAllowed() ? ex.toString() : "Ask to maintainers.";
        body.put("message", message);

        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {UnhandledException.class})
    public ResponseEntity<?> unhandledException(UnhandledException ex) {
        catchAsSentry(ex);
        log.error("Unhandled exception: {}" + ex.toString());
        printStackTraceIfAllowed(ex);

        Map<String, Object> body = new HashMap<>();
        String message = isClientResponseAllowed() ? ex.toString() : "Ask to maintainers.";
        body.put("message", message);

        return new ResponseEntity<>(body, ex.getStatus());
    }

    @ExceptionHandler(value = {HandledException.class})
    public ResponseEntity<?> handledException(HandledException ex) {
        catchAsSentry(ex);
        printStackTraceIfAllowed(ex);

        Map<String, Object> body = new HashMap<>();
        body.put("message", ex.getReason());

        return new ResponseEntity<>(body, ex.getStatus());
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<?> handleControllerValidationException(
        MethodArgumentNotValidException ex) {
        return new ResponseEntity<>(ex.getBindingResult().getAllErrors(), HttpStatus.BAD_REQUEST);
    }

    private void catchAsSentry(Exception ex) {
        if (Arrays.asList(environment.getActiveProfiles()).contains("staging")) {
            Sentry.captureException(ex);
        }
    }

    private void printStackTraceIfAllowed(Exception ex) {
        if (log.isWarnEnabled()) {
            log.error("Handled exception: {}", ex);
            log.warn("Stack trace starts from here");
            Arrays.stream(ex.getStackTrace()).forEach(st -> {
                log.warn(st.toString());
            });
            log.warn("Stack trace finished.");
        }
    }

    private boolean isClientResponseAllowed() {
        return log.isInfoEnabled();
    }
}
