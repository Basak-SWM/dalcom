package com.basak.dalcom.domain.profiles.controllers;

import com.basak.dalcom.domain.profiles.controllers.dto.CoachProfileUpdateReq;
import com.basak.dalcom.domain.profiles.service.CoachProfileService;
import com.basak.dalcom.spring.security.service.DalcomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController("/api/v1/coach-profile")
public class CoachingProfileController {

    public final CoachProfileService coachProfileService;

    @Operation(summary = "코치 프로필 수정")
    @Parameter(name = "userDetails", hidden = true)
    @ApiResponse(responseCode = "200", description = "코치 프로필 수정 성공")
    @ApiResponse(responseCode = "400", description = "코치 프로필이 존재하지 않는 경우")
    @PutMapping
    public ResponseEntity<Void> update(
        @AuthenticationPrincipal DalcomUserDetails userDetails,
        @Validated @RequestBody CoachProfileUpdateReq dto
    ) {
        Integer userId = Integer.parseInt(userDetails.getUsername());
        coachProfileService.update(userId,
            dto.getShortIntroduce(), dto.getSpeciality(), dto.getIntroduce(), dto.getYoutubeUrl());
        return ResponseEntity.ok().build();
    }
}
