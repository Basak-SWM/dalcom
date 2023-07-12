package com.basak.dalcom.domain.common.service.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ObjectNotFoundException extends RuntimeException {

    protected final String objectName;
    protected final String searchCondition;
}
