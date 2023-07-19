package com.basak.dalcom.domain.core.speech.controller;

import com.basak.dalcom.domain.core.audio_segment.data.AudioSegment;
import com.basak.dalcom.domain.core.audio_segment.service.AudioSegmentService;
import com.basak.dalcom.domain.core.speech.controller.dto.SpeechDto;
import com.basak.dalcom.domain.core.speech.controller.dto.UrlDto;
import com.basak.dalcom.domain.core.speech.data.Speech;
import com.basak.dalcom.domain.core.speech.service.SpeechService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.URL;
import java.util.List;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "speeches")
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/presentations/{presentation-id}/speeches")
public class SpeechController {

    private final SpeechService speechService;
    private final AudioSegmentService audioSegmentService;

    @Operation(
        summary = "최초 스피치 생성 API",
        description = "하나의 프레젠테이션에서 스피치 녹음 시작하면 동작하는 API"
    )
    @ApiResponse(responseCode = "201", description = "스피치 생성 성공")
    @ApiResponse(responseCode = "404", description = "전달된 presentationId를 가지는 프레젠테이션이 존재하지 않는 경우",
        content = @Content(schema = @Schema(implementation = Void.class)))
    @PostMapping("")
    public ResponseEntity<SpeechDto> createSpeech(
        @Parameter(name = "presentation-id")
        @PathVariable(name = "presentation-id") Integer presentationId) {
        Speech speech = speechService.createSpeech(presentationId);
        return new ResponseEntity<>(
            new SpeechDto(speech),
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
    @PostMapping("/{speech-id}/record-done")
    public ResponseEntity<String> speechRecordDone(
        @Parameter(name = "presentation-id")
        @PathVariable(name = "presentation-id") Integer presentationId,
        @Parameter(name = "speech-id")
        @PathVariable(name = "speech-id") Integer speechId) {
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
    @GetMapping("/{speech-id}/get-audio-segment-upload-url")
    public ResponseEntity<UrlDto> getAudioSegmentUploadPresignedUrl(
        @Parameter(name = "presentation-id")
        @PathVariable(name = "presentation-id") Integer presentationId,
        @Parameter(name = "speech-id")
        @PathVariable(name = "speech-id") Integer speechId,
        @RequestParam(value = "extension", defaultValue = "webm") String ext) {
        Speech speech = speechService.findSpeechByIdAndPresentationId(speechId, presentationId);
        String key = speechService.getAudioSegmentUploadKey(presentationId, speechId, ext);
        URL presignedUrl = speechService.getAudioSegmentUploadUrl(key);
        return new ResponseEntity<>(new UrlDto(presignedUrl), HttpStatus.OK);
    }

    @Operation(
        summary = "Presigned URL로 업로드 완료 처리 API",
        description = "서버에서 발급한 Presigned URL로 업로드가 완료된 것으로 상태를 갱신하는 API"
    )
    @ApiResponse(responseCode = "201", description = "업로드 완료 처리 성공 (AudioSegment 생성 완료)",
        content = @Content(schema = @Schema(implementation = Void.class)))
    @PostMapping("/{speech-id}/audio-segment-upload-complete")
    public ResponseEntity<Void> audioSegmentUploadComplete(
        @Parameter(name = "presentation-id")
        @PathVariable(name = "presentation-id") Integer presentationId,
        @Parameter(name = "speech-id")
        @PathVariable(name = "speech-id") Integer speechId,
        @Valid @RequestBody UrlDto dto
    ) {
        AudioSegment audioSegment = audioSegmentService.createAudioSegment(speechId, dto.getUrl());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(
        summary = "단일 스피치 조회 API",
        description = "단일 스피치 정보 가져오는 API"
    )
    @ApiResponse(responseCode = "200", description = "스피치 정보 반환 성공")
    @ApiResponse(responseCode = "404", description = "해당 SpeechId를 가지는 스피치가 존재하지 않는 경우",
        content = @Content(schema = @Schema(implementation = Void.class)))
    @GetMapping("/{speech-id}")
    public ResponseEntity<SpeechDto> getSpeech(
        @Parameter(name = "presentation-id")
        @PathVariable(name = "presentation-id") Integer presentationId,
        @Parameter(name = "speech-id")
        @PathVariable(name = "speech-id") Integer speechId) {
        Speech speech = speechService.findSpeechByIdAndPresentationId(speechId, presentationId);
        return new ResponseEntity<>(new SpeechDto(speech), HttpStatus.OK);
    }

    @Operation(
        summary = "스피치 목록 조회 API",
        description = "해당 Presentation 내 스피치 목록 조회하는 API"
    )
    @ApiResponse(responseCode = "200", description = "스피치 정보 반환 성공",
        content = @Content(array = @ArraySchema(schema = @Schema(implementation = SpeechDto.class))))
    @ApiResponse(responseCode = "404", description = "해당 PresentationId를 가지는 스피치가 존재하지 않는 경우",
        content = @Content(schema = @Schema(implementation = Void.class)))
    @GetMapping("")
    public ResponseEntity<List<SpeechDto>> getSpeechList(
        @Parameter(name = "presentation-id")
        @PathVariable(name = "presentation-id") Integer presentationId) {
        List<Speech> speeches = speechService.findSpeechesByPresentationId(presentationId);
        List<SpeechDto> dtos = speeches.stream()
            .map(SpeechDto::new)
            .toList();
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }
}
