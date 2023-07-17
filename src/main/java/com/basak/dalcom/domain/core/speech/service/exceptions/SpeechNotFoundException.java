package com.basak.dalcom.domain.core.speech.service.exceptions;

import com.basak.dalcom.domain.common.service.exceptions.ObjectNotFoundException;

public class SpeechNotFoundException extends ObjectNotFoundException {

    public SpeechNotFoundException(String searchCondition) {
        super("speech", searchCondition);
    }
}
