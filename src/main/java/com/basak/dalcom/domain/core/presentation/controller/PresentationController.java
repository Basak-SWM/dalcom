package com.basak.dalcom.domain.core.presentation.controller;

import com.basak.dalcom.domain.accounts.service.exceptions.AccountNotFoundException;
import com.basak.dalcom.domain.common.controllers.exceptions.NotFoundResponseException;
import com.basak.dalcom.domain.core.presentation.controller.request_dto.PresentationCreateDto;
import com.basak.dalcom.domain.core.presentation.controller.response_dto.PresentationCreateSuccessDto;
import com.basak.dalcom.domain.core.presentation.data.Presentation;
import com.basak.dalcom.domain.core.presentation.service.PresentationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "presentations", description = "프레젠테이션 관련 API")
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/presentations")
public class PresentationController {

    private final PresentationService presentationService;

    @Operation(
        summary = "새 프레젠테이션 생성 API",
        description = "새 프레젠테이션 생성을 수행하는 API"
    )
    @ApiResponse(responseCode = "201", description = "프레젠테이션 생성 성공",
        content = @Content(schema = @Schema(implementation = PresentationCreateDto.class)))
    @ApiResponse(responseCode = "400", description = "Validation 실패",
        content = @Content(schema = @Schema(implementation = Void.class)))
    @ApiResponse(responseCode = "404", description = "전달된 uuid를 가지는 사용자가 존재하지 않는 경우",
        content = @Content(schema = @Schema(implementation = Void.class)))
    @PostMapping("")
    public ResponseEntity<PresentationCreateSuccessDto> createPresentation(
        @Valid @RequestBody PresentationCreateDto dto)
        throws NotFoundResponseException {
        try {
            Presentation presentation = presentationService.createPresentation(dto);
            return new ResponseEntity<>(
                new PresentationCreateSuccessDto(presentation),
                HttpStatus.CREATED
            );
        } catch (AccountNotFoundException exception) {
            throw new NotFoundResponseException(
                exception.getObjectName() + " : " + exception.getSearchCondition());
        }
    }
}
