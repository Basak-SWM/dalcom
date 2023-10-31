package com.basak.dalcom.domain.coaching_request.controller;

import com.basak.dalcom.domain.accounts.service.AccountService;
import com.basak.dalcom.domain.coaching_request.controller.dto.CoachingRequestCreateNotFoundResp;
import com.basak.dalcom.domain.coaching_request.controller.dto.CoachingRequestCreateReq;
import com.basak.dalcom.domain.coaching_request.controller.dto.CoachingRequestCreateResp;
import com.basak.dalcom.spring.security.service.DalcomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "CoachingRequest", description = "코칭 의뢰 API")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/coaching-request")
public class CoachingRequestController {

    private final AccountService accountService;

    @Operation(summary = "코칭 의뢰 생성 API")
    @Parameter(name = "userDetails", hidden = true)
    @ApiResponse(responseCode = "201", description = "코칭 의뢰 생성 성공",
        content = @Content(schema = @Schema(implementation = CoachingRequestCreateResp.class)))
    @ApiResponse(responseCode = "400", description = "STT가 수행되지 않아 스크립트가 없는 스피치인 경우")
    @ApiResponse(responseCode = "404", description = "존재하지 않는 스피치이거나, 존재하지 않는 코치인 경우",
        content = @Content(schema = @Schema(implementation = CoachingRequestCreateNotFoundResp.class)))
    @PostMapping
    public ResponseEntity<Void> create(
        @AuthenticationPrincipal DalcomUserDetails userDetails,
        @Validated @RequestBody CoachingRequestCreateReq dto) {
//        Integer userId = Integer.parseInt(userDetails.getUsername());
        log.info("coach UUID: {}", dto.getCoachUuid());
        return ResponseEntity.ok().build();
    }
}
