package com.basak.dalcom.domain.common.exception.stereotypes;

import com.basak.dalcom.domain.common.exception.HandledException;
import org.springframework.http.HttpStatus;

public class NotFoundException extends HandledException {

    public NotFoundException(String entityName) {
        super(HttpStatus.NOT_FOUND, entityName + " not found.");
    }
}
