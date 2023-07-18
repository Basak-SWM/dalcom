package com.basak.dalcom.domain.common.controllers.exceptions;

import com.basak.dalcom.domain.common.exception.ErrorResponseDto;
import java.util.Optional;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
class NotFoundExceptionResponseDto extends ErrorResponseDto {

    private final String entityName;

    private final String condition;

    @Builder
    public NotFoundExceptionResponseDto(String entityName, String condition,
        Optional<Integer> detailCode, String reason) {
        super(detailCode, reason);
        this.entityName = entityName;
        this.condition = condition;
    }
}

@Getter
@Builder
public class NotFoundException extends HandledException {

    private final String entityName;
    private final String condition;

    @Override
    public ResponseEntity<NotFoundExceptionResponseDto> toResponseEntity() {
        NotFoundExceptionResponseDto.builder()
            .entityName(entityName)
            .condition(condition)
            .detailCode(detailCode)

        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body();
    }
}