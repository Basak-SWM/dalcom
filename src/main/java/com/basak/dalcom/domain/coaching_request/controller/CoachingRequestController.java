package com.basak.dalcom.domain.coaching_request.controller;

import com.basak.dalcom.domain.accounts.service.AccountService;
import com.basak.dalcom.domain.coaching_request.controller.dto.CoachingRequestCreateNotFoundResp;
import com.basak.dalcom.domain.coaching_request.controller.dto.CoachingRequestCreateReq;
import com.basak.dalcom.domain.coaching_request.controller.dto.CoachingRequestDenialReq;
import com.basak.dalcom.domain.coaching_request.controller.dto.CoachingRequestResp;
import com.basak.dalcom.spring.security.service.DalcomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
        content = @Content(schema = @Schema(implementation = CoachingRequestResp.class)))
    @ApiResponse(responseCode = "400", description = "STT가 수행되지 않아 스크립트가 없는 스피치인 경우")
    @ApiResponse(responseCode = "404", description = "존재하지 않는 스피치이거나, 존재하지 않는 코치인 경우",
        content = @Content(schema = @Schema(implementation = CoachingRequestCreateNotFoundResp.class)))
    @PostMapping
    public ResponseEntity<CoachingRequestResp> create(
        @AuthenticationPrincipal DalcomUserDetails userDetails,
        @Validated @RequestBody CoachingRequestCreateReq dto) {
//        Integer userId = Integer.parseInt(userDetails.getUsername());
        log.info("coach UUID: {}", dto.getCoachUuid());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "코칭 의뢰 목록 조회 API",
        description = "본인에게 연결된 코칭 의뢰 목록을 조회하는 API")
    @Parameter(name = "userDetails", hidden = true)
    @ApiResponse(responseCode = "200", description = "코칭 의뢰 목록 조회 성공",
        content = @Content(array = @ArraySchema(schema = @Schema(implementation = CoachingRequestResp.class))))
    @GetMapping
    public ResponseEntity<List<CoachingRequestResp>> getList(
        @AuthenticationPrincipal DalcomUserDetails userDetails) {
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "코칭 의뢰 단건 조회 API")
    @Parameter(name = "userDetails", hidden = true)
    @ApiResponse(responseCode = "200", description = "코칭 의뢰 조회 성공",
        content = @Content(schema = @Schema(implementation = CoachingRequestResp.class)))
    @ApiResponse(responseCode = "401", description = "'코칭 진행 중'과 같은 이유로 접근이 불가능할 경우")
    @ApiResponse(responseCode = "404", description = "존재하지 않는 코칭 의뢰인 경우")
    @GetMapping("/{coaching-request-id}")
    public ResponseEntity<CoachingRequestResp> get(
        @AuthenticationPrincipal DalcomUserDetails userDetails,
        @Parameter(name = "coaching-request-id")
        @PathVariable(name = "coaching-request-id") Long coachingRequestId) {
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "코칭 요청 취소(삭제) API")
    @Parameter(name = "userDetails", hidden = true)
    @ApiResponse(responseCode = "204", description = "코칭 요청 삭제 성공")
    @ApiResponse(responseCode = "400", description = "수락할 수 없는 코칭 의뢰인 경우")
    @ApiResponse(responseCode = "404", description = "존재하지 않는 코칭 의뢰인 경우")
    @DeleteMapping("/{coaching-request-id}")
    public ResponseEntity<Void> delete(
        @AuthenticationPrincipal DalcomUserDetails userDetails,
        @Parameter(name = "coaching-request-id")
        @PathVariable(name = "coaching-request-id") Long coachingRequestId) {
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "코칭 수락 API")
    @Parameter(name = "userDetails", hidden = true)
    @ApiResponse(responseCode = "200", description = "코칭 수락 성공")
    @ApiResponse(responseCode = "400", description = "수락할 수 없는 코칭 의뢰인 경우")
    @ApiResponse(responseCode = "404", description = "존재하지 않는 코칭 의뢰인 경우")
    @PostMapping("/{coaching-request-id}/accept")
    public ResponseEntity<Void> accept(
        @AuthenticationPrincipal DalcomUserDetails userDetails,
        @Parameter(name = "coaching-request-id")
        @PathVariable(name = "coaching-request-id") Long coachingRequestId) {
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "코칭 거절 API")
    @Parameter(name = "userDetails", hidden = true)
    @ApiResponse(responseCode = "200", description = "코칭 거절 성공")
    @ApiResponse(responseCode = "400", description = "거절할 수 없는 코칭 의뢰인 경우")
    @ApiResponse(responseCode = "404", description = "존재하지 않는 코칭 의뢰인 경우")
    @PostMapping("/{coaching-request-id}/deny")
    public ResponseEntity<Void> deny(
        @AuthenticationPrincipal DalcomUserDetails userDetails,
        @Parameter(name = "coaching-request-id")
        @PathVariable(name = "coaching-request-id") Long coachingRequestId,
        @Validated @RequestBody CoachingRequestDenialReq dto) {
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "코칭 완료 API")
    @Parameter(name = "userDetails", hidden = true)
    @ApiResponse(responseCode = "200", description = "코칭 완료 성공")
    @ApiResponse(responseCode = "400", description = "완료할 수 없는 코칭 의뢰인 경우")
    @ApiResponse(responseCode = "404", description = "존재하지 않는 코칭 의뢰인 경우")
    @PostMapping("/{coaching-request-id}/finish")
    public ResponseEntity<Void> finish(
        @AuthenticationPrincipal DalcomUserDetails userDetails,
        @Parameter(name = "coaching-request-id")
        @PathVariable(name = "coaching-request-id") Long coachingRequestId) {
        return ResponseEntity.ok().build();
    }
}
