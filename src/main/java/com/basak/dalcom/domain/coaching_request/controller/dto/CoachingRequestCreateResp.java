package com.basak.dalcom.domain.coaching_request.controller.dto;

import java.net.URL;
import java.util.Optional;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CoachingRequestCreateResp {


    private Long id;
    private Optional<String> userMessage; // 코칭 요청 메시지

    // From Accounts
    private UUID userUuid; // 의뢰 사용자 account uuid
    private UUID coachUuid; // 피의뢰 코치 account uuid

    // From Presentation
    private String title; // 프레젠테이션 제목
    private Optional<String> outline; // 프레젠테이션 개요
    private Optional<String> checkpoint; // 프레젠테이션 잘 하고 싶은 점

    // From Speech
    private URL fullAudioUrl; // 음성이 저장된 url
    private String sttResult; // stt 결과 (Stringfied json)
}
