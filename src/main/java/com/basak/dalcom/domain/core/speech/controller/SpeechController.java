package com.basak.dalcom.domain.core.speech.controller;

import com.basak.dalcom.domain.core.speech.controller.response_dto.GetSpeechDto;
import com.basak.dalcom.domain.core.speech.controller.response_dto.SpeechCreateSuccessDto;
import com.basak.dalcom.domain.core.speech.data.Speech;
import com.basak.dalcom.domain.core.speech.service.SpeechService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/presentations/{presentationId}/speeches")
public class SpeechController {

    private final SpeechService speechService;

    @Operation(
        summary = "최초 스피치 생성 API",
        description = "하나의 프레젠테이션에서 스피치 녹음 시작하면 동작하는 API"
    )
    @ApiResponse(responseCode = "201", description = "스피치 생성 성공")
    @ApiResponse(responseCode = "404", description = "전달된 presentationId를 가지는 프레젠테이션이 존재하지 않는 경우",
        content = @Content(schema = @Schema(implementation = Void.class)))
    @PostMapping("")
    public ResponseEntity<SpeechCreateSuccessDto> createSpeech(
        @PathVariable Integer presentationId) {
        Speech speech = speechService.createSpeech(presentationId);
        return new ResponseEntity<>(
            new SpeechCreateSuccessDto(speech),
            HttpStatus.CREATED
        );
    }

    @Operation(
        summary = "스피치 녹음 완료 API",
        description = "스피치 녹음 완료 후 분석 시작 요청하는 API"
    )
    @ApiResponse(responseCode = "200", description = "스피치 분석 시작 성공")
    @ApiResponse(responseCode = "404", description = "전달된 speechId 가지는 개체가 존재하지 않는 경우",
        content = @Content(schema = @Schema(implementation = Void.class)))
    @PostMapping("/{speechId}/record-done")
    public ResponseEntity<String> speechRecordDone(@PathVariable Integer speechId) {
        speechService.speechRecordDoneAndStartAnalyze(speechId);
        return new ResponseEntity<>(
            "Success",
            HttpStatus.OK
        );
    }

    @Operation(
        summary = "스피치 녹음 중 업로드 용 Presigned URL 받아오는 API",
        description = "스피치 녹음 중 업로드 용 Presigned URL 받아오는 API"
    )
    @ApiResponse(responseCode = "200", description = "Presigned URL 반환 성공")
    @ApiResponse(responseCode = "404", description = "전달된 presentationId를 가지는 프레젠테이션이 존재하지 않는 경우",
        content = @Content(schema = @Schema(implementation = Void.class)))
    @GetMapping("/{speechId}/get-audio-segment-upload-url")
    public ResponseEntity<String> getAudioSegmentUploadPresignedUrl(@PathVariable Integer speechId) {
        // TODO: Presigned URL 생성
        String DUMMY_PRESIGNED_URL = "https://DUMMY";
        return new ResponseEntity<>(DUMMY_PRESIGNED_URL, HttpStatus.OK);
    }

    @Operation(
        summary = "단일 스피치 조회 API",
        description = "단일 스피치 정보 가져오는 API"
    )
    @ApiResponse(responseCode = "200", description = "스피치 정보 반환 성공")
    @ApiResponse(responseCode = "404", description = "해당 SpeechId를 가지는 스피치가 존재하지 않는 경우",
        content = @Content(schema = @Schema(implementation = Void.class)))
    @GetMapping("/{speechId}")
    public ResponseEntity<GetSpeechDto> getSpeech(@PathVariable Integer speechId) {
        Speech speech = speechService.findSpeechById(speechId);
        return new ResponseEntity<>(new GetSpeechDto(speech), HttpStatus.OK);
    }

    @Operation(
        summary = "스피치 목록 조회 API",
        description = "해당 Presentation 내 스피치 목록 조회하는 API"
    )
    @ApiResponse(responseCode = "200", description = "스피치 정보 반환 성공")
    @ApiResponse(responseCode = "404", description = "해당 SpeechId를 가지는 스피치가 존재하지 않는 경우",
        content = @Content(schema = @Schema(implementation = Void.class)))
    @GetMapping("/{speechId}")
    public ResponseEntity<GetSpeechDto> getSpeechList(@PathVariable Integer speechId) {
        // TODO
        return null;
    }
}
