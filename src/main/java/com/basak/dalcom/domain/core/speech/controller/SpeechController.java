package com.basak.dalcom.domain.core.speech.controller;

import com.basak.dalcom.domain.core.speech.controller.request_dto.SpeechRecordDoneDto;
import com.basak.dalcom.domain.core.speech.controller.response_dto.SpeechCreateSuccessDto;
import com.basak.dalcom.domain.core.speech.controller.response_dto.SpeechRecordDoneSuccessDto;
import com.basak.dalcom.domain.core.speech.data.Speech;
import com.basak.dalcom.domain.core.speech.service.SpeechService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@AllArgsConstructor
@RestController
@RequestMapping("/presentations/{presentationId}/speeches")
public class SpeechController {

    private final SpeechService speechService;

    @Operation(
        summary = "최초 스피치 생성 API",
        description = "하나의 프레젠테이션에서 스피치 녹음 시작하면 동작하는 API"
    )
    @ApiResponse(responseCode = "201", description = "스피치 생성 성공")
    @ApiResponse(responseCode = "400", description = "Validation 실패",
        content = @Content(schema = @Schema(implementation = Void.class)))
    @ApiResponse(responseCode = "404", description = "전달된 presentationId를 가지는 프레젠테이션이 존재하지 않는 경우",
        content = @Content(schema = @Schema(implementation = Void.class)))
    @PostMapping("")
    public ResponseEntity<SpeechCreateSuccessDto> createSpeech(@PathVariable Long presentationId) {
        Speech speech = speechService.createSpeech(presentationId);
        return new ResponseEntity<>(
            new SpeechCreateSuccessDto(speech),
            HttpStatus.CREATED
        );
    }

//    public ResponseEntity<> speechRecordDone(@Valid @RequestBody SpeechRecordDoneDto dto) {}

    @GetMapping("/{speechId}/get-audio-segments-upload-url")
    public ResponseEntity<String> getAudioSegmentUploadPresignedUrl(@PathVariable String speechId) {
    }

//    public ResponseEntity<Speech>
//
//    @Operation(
//        summary = "스피치 생성 API",
//        description = "하나의 프레젠테이션에서 스피치 녹음이 끝나고 완료 버튼 누르면 동작하는 API"
//    )
//    @ApiResponse(responseCode = "201", description = "스피치 생성 성공")
//    @ApiResponse(responseCode = "400", description = "Validation 실패",
//        content = @Content(schema = @Schema(implementation = Void.class)))
//    @ApiResponse(responseCode = "404", description = "전달된 presentationId를 가지는 프레젠테이션이 존재하지 않는 경우",
//        content = @Content(schema = @Schema(implementation = Void.class)))
//    @PostMapping("")
//    public ResponseEntity<SpeechRecordDoneSuccessDto> speechRecordDone(@Valid @RequestBody
//        SpeechRecordDoneDto dto) {
//        Speech speech = speechService.createSpeech(dto);
//
//        return new ResponseEntity<>()
//    }
}
