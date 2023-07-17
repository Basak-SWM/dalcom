package com.basak.dalcom.domain.common.exception;

import com.basak.dalcom.domain.common.service.exceptions.DuplicatedFieldException;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * javax.validation.Valid or @Validated 으로 binding error 발생시 발생.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
        MethodArgumentNotValidException ex) {
        log.error("handleMethodArgumentNotValidException", ex);
        BindingResult bindingResult = ex.getBindingResult();
        StringBuilder stringBuilder = new StringBuilder();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            stringBuilder.append(fieldError.getField()).append(":");
            stringBuilder.append(fieldError.getDefaultMessage());
            stringBuilder.append(", ");
        }
        final ErrorResponse response = ErrorResponse.of(ErrorCode.BAD_REQUEST,
            String.valueOf(stringBuilder));
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * Unique Key 위반 시 발생
     */
    @ExceptionHandler(DuplicatedFieldException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<ErrorResponse> handleNotUniqueExceptions(DuplicatedFieldException ex) {
        log.error("Field Duplicated Exception");
        final ErrorResponse response = ErrorResponse.of(ErrorCode.FIELD_CONFLICT, ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    /**
     * Request Body Json Parsing 실패 시
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleRequestBodyParsingException(
        HttpMessageNotReadableException ex) {
        if (ex.getCause() instanceof JsonProcessingException) {
            log.error("Request Body Json Parsing Failed");
            final ErrorResponse response = ErrorResponse.of(ErrorCode.BAD_REQUEST,"Request Body Json Parsing Failed");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        return handleAllException(ex);
    }


    /**
     * Unhandled Error 발생 시
     */
    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleAllException(Exception ex) {
        log.error("UNHANDLED_EXCEPTION", ex.getClass().getCanonicalName());
        log.error("UNHANDLED_EXCEPTION", ex.getCause().getMessage());
        log.error("UNHANDLED_EXCEPTION", ex);
        // 서버 내부 에러는 상세 내용 Cient으로 전송하지 않는다.
        final ErrorResponse response = ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
