package com.basak.dalcom.domain.coaching_request.service.dto;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class CoachingRequestCreateDto {

    private Integer speechId;
    private UUID coachProfileId;
    private UUID userProfileId;
    private String userMessage;
}
