package com.basak.dalcom.domain.common.service.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DuplicatedFieldException extends RuntimeException {

    protected final String fieldName;
}
