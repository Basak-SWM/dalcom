package com.basak.dalcom.domain.profiles.controllers;

import com.basak.dalcom.domain.profiles.controllers.dto.CoachProfileUpdateReq;
import com.basak.dalcom.domain.profiles.controllers.dto.PublicCoachProfileRespDto;
import com.basak.dalcom.domain.profiles.data.CoachProfile;
import com.basak.dalcom.domain.profiles.service.CoachProfileService;
import com.basak.dalcom.spring.security.service.DalcomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@Tag(name = "coach-profile", description = "코치 프로필 관련 API")
@RestController
@RequestMapping("/api/v1/coach-profile")
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

    @Operation(summary = "코치 프로필 목록 조회")
    @Parameter(name = "userDetails", hidden = true)
    @ApiResponse(responseCode = "200", description = "코치 프로필 목록 조회 성공")
    @GetMapping
    public ResponseEntity<List<PublicCoachProfileRespDto>> findAll(
        @AuthenticationPrincipal DalcomUserDetails userDetails
    ) {
        List<CoachProfile> coachProfileList = coachProfileService.findAll();
        List<PublicCoachProfileRespDto> dtoList = coachProfileList.stream()
            .map(PublicCoachProfileRespDto::fromEntity)
            .toList();
        return ResponseEntity.ok(dtoList);
    }
}
