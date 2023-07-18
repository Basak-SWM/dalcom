package com.basak.dalcom.domain.common.controllers.exceptions;

import com.basak.dalcom.domain.common.exception.ErrorResponseDto;
import com.basak.dalcom.domain.common.exception.ErrorStatusWrapper;
import java.util.Arrays;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

@Getter
@Builder
abstract public class ControllerException extends RuntimeException {

    protected ErrorStatusWrapper errorStatusWrapper;
    protected String message;

    public abstract ResponseEntity<? extends ErrorResponseDto> toResponseEntity();

    @Override
    public String toString() {
        String header = String.format("[%d-%d] %s ()",
            errorStatusWrapper.getStatus().value(),
            errorStatusWrapper.getDetailCode().orElse(null),
            message);
        String stackTrace = String.join(System.lineSeparator(),
            Arrays.toString(this.getStackTrace()));

        return header + System.lineSeparator() + stackTrace;
    }
}
