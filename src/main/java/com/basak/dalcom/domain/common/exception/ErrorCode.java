package com.basak.dalcom.domain.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    INVALID_INPUT_VALUE_BINDING_ERROR(400,  "Invalid Input Value Binding"),
    FIELD_CONFLICT(409, "Given value is already used"),
    BAD_REQUEST(400, "Bad Request"),

    INTERNAL_SERVER_ERROR(500, "Something went wrong");

    private final int status;
    private final String message;
}
