package com.basak.dalcom.domain.common.exception;

import java.util.Optional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public class ErrorStatusWrapper {

    private final HttpStatus status;
    private final Optional<Integer> detailCode;
}
