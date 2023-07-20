package com.basak.dalcom.domain.core.presentation.controller;

import com.basak.dalcom.domain.core.presentation.controller.dto.PresentationCreateDto;
import com.basak.dalcom.domain.core.presentation.controller.dto.PresentationRespDto;
import com.basak.dalcom.domain.core.presentation.data.Presentation;
import com.basak.dalcom.domain.core.presentation.service.PresentationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "presentations", description = "프레젠테이션 관련 API")
@AllArgsConstructor
@Slf4j
@RestController
@RequestMapping("/api/v1/presentations")
public class PresentationController {

    private final PresentationService presentationService;

    @Operation(
        summary = "새 프레젠테이션 생성 API",
        description = "새 프레젠테이션 생성을 수행하는 API"
    )
    @ApiResponse(responseCode = "201", description = "프레젠테이션 생성 성공",
        content = @Content(schema = @Schema(implementation = PresentationRespDto.class)))
    @ApiResponse(responseCode = "404", description = "전달된 uuid를 가지는 사용자가 존재하지 않는 경우",
        content = @Content(schema = @Schema(implementation = Void.class)))
    @PostMapping("")
    public ResponseEntity<PresentationRespDto> createPresentation(
        @Valid @RequestBody PresentationCreateDto dto) {
        PresentationCreateDto.PresentationDto presentationDto = dto.getPresentation();
        UUID uuid = UUID.fromString(dto.getAccountUuid());

        Presentation presentation = presentationService
            .createPresentation(uuid, presentationDto.toServiceDto());

        return new ResponseEntity<>(
            new PresentationRespDto(presentation),
            HttpStatus.CREATED
        );
    }

    @Operation(
        summary = "목록 조회 API",
        description = "요청된 uuid를 가지는 계정이 생성한 모든 프레젠테이션 목록을 반환하는 API"
    )
    @ApiResponse(responseCode = "200", description = "조회 성공",
        content = @Content(array = @ArraySchema(schema = @Schema(implementation = PresentationRespDto.class))))
    @ApiResponse(responseCode = "404", description = "전달된 uuid를 가지는 사용자가 존재하지 않는 경우",
        content = @Content(schema = @Schema(implementation = Void.class)))
    @GetMapping("")
    public ResponseEntity<List<PresentationRespDto>> getOwnPresentations(
        @RequestParam(value = "account-uuid") String accountUuid) {
        UUID uuid = UUID.fromString(accountUuid);

        List<PresentationRespDto> presentationDtoList = presentationService
            .getPresentationsByAccountUuid(uuid).stream()
            .map(PresentationRespDto::new)
            .toList();
        return new ResponseEntity<>(presentationDtoList, HttpStatus.OK);
    }
}
