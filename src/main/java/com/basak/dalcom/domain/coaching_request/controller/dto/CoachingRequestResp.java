package com.basak.dalcom.domain.coaching_request.controller.dto;

import com.basak.dalcom.domain.coaching_request.CoachingRequest.Status;
import com.basak.dalcom.domain.coaching_request.data.CoachingRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CoachingRequestResp {

    @Schema(description = "코칭 요청 ID", type = "long", example = "1")
    private Long id;
    @Schema(description = "코칭 요청 메시지", type = "string", example = "꼼꼼하게 해주세요")
    private String userMessage;
    @Schema(description = "코칭 상태", type = "string", example = "REQUESTED")
    private Status status; // 코칭 상태

    // From Accounts
    @Schema(description = "의뢰 사용자 account uuid", type = "UUID", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID userUuid;
    @Schema(description = "의뢰 코치 account 이름", type = "UUID", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID coachUuid;

    @Schema(description = "의뢰 사용자 닉네임", type = "string", example = "김바삭")
    private String userNickname;
    @Schema(description = "의뢰 코치 닉네임", type = "string", example = "박코치")
    private String coachNickname;

    // From Presentation
    @Schema(description = "프레젠테이션 ID", type = "integer", example = "1")
    private Integer presentationId;
    @Schema(description = "프레젠테이션 제목", type = "string", example = "술 빨리 깨는 방법")
    private String title;
    @Schema(description = "프레젠테이션 개요", type = "string", example = "술 빨리 깨는 방법에 대한 설명")
    private String outline;
    @Schema(description = "잘 하고 싶은 점", type = "string", example = "약간 취해 보이는 것처럼")
    private String checkpoint;


    @Schema(description = "스피치 ID", type = "integer", example = "2")
    private Integer speechId;
    // From Speech
    @Schema(description = "음성이 저장된 url", type = "URL", example = "https://dalcom.s3.ap-northeast-2.amazonaws.com/audios/1.wav")
    private URL fullAudioUrl;
    @Schema(description = "STT 결과", type = "string")
    private String sttResult;

    // Coaching
    @Schema(description = "코칭 코멘트", type = "string", example = "싹 갈아 엎으세요")
    private String coachMessage;
    @Schema(description = "코치가 쓴 사용자 기호", type = "string")
    private String jsonUserSymbol;

    public static CoachingRequestResp fromEntity(CoachingRequest entity)
        throws MalformedURLException {
        return new CoachingRequestResp(
            entity.getId(),
            entity.getUserMessage(),
            entity.getStatus(),
            UUID.fromString(entity.getUserProfile().getAccount().getUuid()),
            UUID.fromString(entity.getCoachProfile().getAccount().getUuid()),
            entity.getUserProfile().getAccount().getNickname(),
            entity.getCoachProfile().getAccount().getNickname(),
            entity.getPresentationId(),
            entity.getTitle(),
            entity.getOutline(),
            entity.getCheckpoint(),
            entity.getSpeechId(),
            new URL(entity.getFullAudioUrl()),
            entity.getSttResult(),
            entity.getCoachMessage(),
            entity.getJsonUserSymbol()
        );
    }
}
