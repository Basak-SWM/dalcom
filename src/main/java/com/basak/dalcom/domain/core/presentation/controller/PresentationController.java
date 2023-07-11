package com.basak.dalcom.domain.core.presentation.controller;

import com.basak.dalcom.domain.core.presentation.controller.request_dto.PresentationCreateDto;
import com.basak.dalcom.domain.core.presentation.data.Presentation;
import com.basak.dalcom.domain.core.presentation.service.PresentationService;
import com.basak.dalcom.domain.profiles.data.UserProfileRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/presentations")
public class PresentationController {

    private final PresentationService presentationService;
    private final UserProfileRepository userProfileRepository;

    @Operation(
        summary = "새 프레젠테이션 생성 API",
        description = "새 프레젠테이션 생성을 수행하는 API"
    )
    @ApiResponse(responseCode = "201", description = "프레젠테이션 생성 성공",
        content = @Content(schema = @Schema(implementation = PresentationCreateDto.class)))
    @PostMapping("")
    public Presentation createPresentation(@Valid @RequestBody PresentationCreateDto dto) {
        return null;
    }
}
