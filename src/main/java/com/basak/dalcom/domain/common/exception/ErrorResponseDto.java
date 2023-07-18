package com.basak.dalcom.domain.common.exception;

import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

/**
 * Global Exception Handler에서 발생한 에러에 대한 응답 처리를 관리
 */
@Builder
@Getter
@AllArgsConstructor
abstract public class ErrorResponseDto {

    private final Optional<Integer> detailCode;
    private final String reason;

    public ResponseEntity<? extends ErrorResponseDto> toResponseEntity() {

    }
}