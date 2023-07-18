package com.basak.dalcom.domain.core.presentation.controller;

import com.basak.dalcom.domain.core.presentation.controller.dto.PresentationCreateDto;
import com.basak.dalcom.domain.core.presentation.controller.dto.PresentationCreateSuccessDto;
import com.basak.dalcom.domain.core.presentation.controller.dto.PresentationDto;
import com.basak.dalcom.domain.core.presentation.controller.dto.PresentationDtoSlice;
import com.basak.dalcom.domain.core.presentation.data.Presentation;
import com.basak.dalcom.domain.core.presentation.service.PresentationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
        @Valid @RequestBody PresentationCreateDto dto) {
        Presentation presentation = presentationService.createPresentation(dto);
        return new ResponseEntity<>(
            new PresentationCreateSuccessDto(presentation),
            HttpStatus.CREATED
        );
    }

    @Operation(
        summary = "프레젠테이션 본인 목록 조회 API",
        description = "요청된 uuid를 가지는 계정이 생성한 모든 프레젠테이션 목록을 반환하는 API"
    )
    @ApiResponse(responseCode = "200", description = "조회 성공",
        content = @Content(schema = @Schema(implementation = PresentationDtoSlice.class)))
    @ApiResponse(responseCode = "404", description = "전달된 uuid를 가지는 사용자가 존재하지 않는 경우",
        content = @Content(schema = @Schema(implementation = Void.class)))
    @GetMapping("")
    public ResponseEntity<Slice<PresentationDto>> getOwnPresentations(String accountUuid,
        Pageable pageable) {
        Slice<Presentation> presentations = presentationService
            .getPresentationsOf(accountUuid, pageable);
        Slice<PresentationDto> presentationDtos = presentations.map(PresentationDto::new);
        return new ResponseEntity<>(presentationDtos, HttpStatus.OK);
    }
}
