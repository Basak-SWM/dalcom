package com.basak.dalcom.domain.common.exception.custom;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DuplicatedFieldException extends RuntimeException {

    protected final String fieldName;
}
