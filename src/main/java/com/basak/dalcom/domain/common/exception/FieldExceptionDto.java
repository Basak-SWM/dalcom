package com.basak.dalcom.domain.common.exception;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;

@Getter
public class FieldExceptionDto {

    private final List<Dto> dtoList;

    public FieldExceptionDto(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();

        dtoList = new ArrayList<>();
        bindingResult.getFieldErrors().forEach(fieldError -> {
            String field = fieldError.getField();
            Object rejectedValue = fieldError.getRejectedValue();
            String message = fieldError.getDefaultMessage();
            String code = fieldError.getCode();
            dtoList.add(new Dto(field, rejectedValue, message, code));
        });
    }

    record Dto(
        String field,
        Object rejectedValue,
        String message,
        String code
    ) {

    }
}
