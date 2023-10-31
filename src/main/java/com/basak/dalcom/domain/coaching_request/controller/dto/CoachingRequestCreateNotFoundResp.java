package com.basak.dalcom.domain.coaching_request.controller.dto;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CoachingRequestCreateNotFoundResp {

    private final DetailCode detailCode;

    public enum DetailCode {
        COACH, SPEECH
    }
}
