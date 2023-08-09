package com.basak.dalcom.domain.core.speech.controller;

import com.basak.dalcom.aws.s3.S3Service;
import com.basak.dalcom.aws.s3.presigned_url.PresignedURLService;
import com.basak.dalcom.domain.common.exception.UnhandledException;
import com.basak.dalcom.domain.common.exception.stereotypes.ConflictException;
import com.basak.dalcom.domain.core.analysis_result.data.AnalysisType;
import com.basak.dalcom.domain.core.analysis_result.service.AnalysisRecordService;
import com.basak.dalcom.domain.core.audio_segment.controller.dto.AudioSegmentRespDto;
import com.basak.dalcom.domain.core.audio_segment.data.AudioSegment;
import com.basak.dalcom.domain.core.audio_segment.service.AudioSegmentService;
import com.basak.dalcom.domain.core.audio_segment.service.dto.CreateAudioSegmentDto;
import com.basak.dalcom.domain.core.speech.controller.dto.AIChatLogCreateResDto;
import com.basak.dalcom.domain.core.speech.controller.dto.AIChatLogListRespDto;
import com.basak.dalcom.domain.core.speech.controller.dto.AIChatLogReqDto;
import com.basak.dalcom.domain.core.speech.controller.dto.AIChatLogRespDto;
import com.basak.dalcom.domain.core.speech.controller.dto.PresignedUrlReqDto;
import com.basak.dalcom.domain.core.speech.controller.dto.SpeechCreateDto;
import com.basak.dalcom.domain.core.speech.controller.dto.SpeechRespDto;
import com.basak.dalcom.domain.core.speech.controller.dto.SpeechUpdateReqDto;
import com.basak.dalcom.domain.core.speech.controller.dto.UrlDto;
import com.basak.dalcom.domain.core.speech.data.AIChatLog;
import com.basak.dalcom.domain.core.speech.data.Speech;
import com.basak.dalcom.domain.core.speech.service.SpeechService;
import com.basak.dalcom.domain.core.speech.service.dto.AIChatLogRetrieveResult;
import com.basak.dalcom.domain.core.speech.service.dto.SpeechUpdateDto;
import com.basak.dalcom.external_api.openai.service.OpenAIService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.json.JsonParseException;
import org.springframework.boot.json.JsonParser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "speeches")
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/presentations/{presentation-id}/speeches")
public class SpeechController {

    private final SpeechService speechService;
    private final AudioSegmentService audioSegmentService;
    private final AnalysisRecordService analysisResultService;
    private final PresignedURLService presignedURLService;
    private final OpenAIService openAIService;
    private final S3Service s3Service;
    private final JsonParser jsonParser = new JacksonJsonParser();

    @Operation(
        summary = "스피치 생성 API",
        description = "특정 프레젠테이션에 소속되는 스피치를 하나 생성한다."
    )
    @ApiResponse(responseCode = "201", description = "스피치 생성 성공")
    @ApiResponse(responseCode = "404", description = "전달된 presentation-id를 가지는 프레젠테이션이 존재하지 않는 경우",
        content = @Content(schema = @Schema(implementation = SpeechRespDto.class)))
    @PostMapping("")
    public ResponseEntity<SpeechRespDto> createSpeech(
        @Parameter(name = "presentation-id")
        @PathVariable(name = "presentation-id") Integer presentationId,
        @RequestBody SpeechCreateDto dto) {
        Optional<Integer> refSpeechId = Optional.ofNullable(dto.getReferenceSpeechId());
        Speech speech = speechService.createSpeech(presentationId, refSpeechId);
        return new ResponseEntity<>(
            new SpeechRespDto(speech),
            HttpStatus.CREATED
        );
    }

    @Operation(
        summary = "단일 스피치 조회 API",
        description = "단일 스피치 정보 가져오는 API"
    )
    @ApiResponse(responseCode = "200", description = "스피치 정보 반환 성공",
        content = @Content(schema = @Schema(implementation = SpeechRespDto.class)))
    @ApiResponse(responseCode = "404", description = "해당 SpeechId를 가지는 스피치가 존재하지 않는 경우",
        content = @Content)
    @GetMapping("/{speech-id}")
    public ResponseEntity<SpeechRespDto> getSpeech(
        @Parameter(name = "presentation-id")
        @PathVariable(name = "presentation-id") Integer presentationId,
        @Parameter(name = "speech-id")
        @PathVariable(name = "speech-id") Integer speechId) {
        Speech speech = speechService.findSpeechByIdAndPresentationId(
            speechId, presentationId, true);

        return new ResponseEntity<>(new SpeechRespDto(speech), HttpStatus.OK);
    }

    @Operation(
        summary = "스피치 목록 조회 API",
        description = "해당 Presentation 내 스피치 목록 조회하는 API"
    )
    @ApiResponse(responseCode = "200", description = "스피치 정보 반환 성공",
        content = @Content(array = @ArraySchema(schema = @Schema(implementation = SpeechRespDto.class))))
    @ApiResponse(responseCode = "404", description = "해당 PresentationId를 가지는 스피치가 존재하지 않는 경우",
        content = @Content)
    @GetMapping("")
    public ResponseEntity<List<SpeechRespDto>> getSpeechList(
        @Parameter(name = "presentation-id")
        @PathVariable(name = "presentation-id") Integer presentationId) {
        List<Speech> speeches = speechService.findSpeechesByPresentationId(presentationId);
        List<SpeechRespDto> dtos = speeches.stream()
            .map(SpeechRespDto::new)
            .toList();
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @Operation(
        summary = "스피치 녹음 완료 통지 API",
        description = "연습(녹음) 종류 후 STT 처리 및 분석 시작을 요청하는 API"
    )
    @ApiResponse(responseCode = "404", description = "전달된 speech-id를 가지는 스피치가 존재하지 않는 경우",
        content = @Content)
    @ApiResponse(responseCode = "409", description = "이미 녹음 완료 처리가 요청된 경우",
        content = @Content)
    @PostMapping("/{speech-id}/record-done")
    public ResponseEntity<Void> speechRecordDone(
        @Parameter(name = "presentation-id")
        @PathVariable(name = "presentation-id") Integer presentationId,
        @Parameter(name = "speech-id")
        @PathVariable(name = "speech-id") Integer speechId) {
        speechService.checkExistence(presentationId, speechId);
        Speech speech = speechService.findSpeechByIdAndPresentationId(
            speechId, presentationId, false
        );

        if (speech.getRecordDone()) {
            throw new ConflictException("Already record done.");
        }

        speechService.speechRecordDoneAndStartAnalyze(speech);
        return new ResponseEntity<>(
            HttpStatus.OK
        );
    }

    @Operation(
        summary = "Audio Segment 업로드용 Presigned URL 발급 API",
        description = "일정 시간 동안만 유효한 업로드용 S3 presigned URL을 발급하는 API로, 해당 메서드의 반환 URL을 " +
            "URL로 하여 PUT 메서드로 파일을 첨부, 업로드하면 S3 bucket에 해당 파일이 업로드 된다. <br/>" +
            "업로드가 완료되면, 업로드가 완료되었음을 통지하는 API를 호출하여 AudioSegment를 생성해야 한다."
    )
    @ApiResponse(responseCode = "200", description = "Presigned URL 반환 성공")
    @ApiResponse(responseCode = "404", description = "전달된 presentationId를 가지는 프레젠테이션이 존재하지 않는 경우",
        content = @Content(schema = @Schema(implementation = UrlDto.class)))
    @PostMapping("/{speech-id}/audio-segments/upload-url")
    public ResponseEntity<UrlDto> getAudioSegmentUploadPresignedUrl(
        @Parameter(name = "presentation-id")
        @PathVariable(name = "presentation-id") Integer presentationId,
        @Parameter(name = "speech-id")
        @PathVariable(name = "speech-id") Integer speechId,
        @Valid @RequestBody PresignedUrlReqDto presignedUrlReqDto) {
        speechService.checkExistence(presentationId, speechId);

        String key = speechService.getAudioSegmentUploadKey(
            presentationId, speechId, presignedUrlReqDto.getExtension().getValue());
        URL presignedUrl = speechService.getAudioSegmentUploadUrl(key);
        return new ResponseEntity<>(new UrlDto(presignedUrl), HttpStatus.OK);
    }

    @Operation(
        summary = "Presigned URL로 업로드 완료 처리 API",
        description = "서버에서 발급한 Presigned URL로 업로드가 완료된 것으로 상태를 갱신하는 API"
    )
    @ApiResponse(responseCode = "201", description = "업로드 완료 처리 성공 (AudioSegment 생성 완료)",
        content = @Content(schema = @Schema(implementation = AudioSegmentRespDto.class)))
    @PostMapping("/{speech-id}/audio-segment/upload-url/done")
    public ResponseEntity<AudioSegmentRespDto> audioSegmentUploadComplete(
        @Parameter(name = "presentation-id")
        @PathVariable(name = "presentation-id") Integer presentationId,
        @Parameter(name = "speech-id")
        @PathVariable(name = "speech-id") Integer speechId,
        @Valid @RequestBody UrlDto requestDto
    ) {
        speechService.checkExistence(presentationId, speechId);

        CreateAudioSegmentDto serviceDto = new CreateAudioSegmentDto(speechId, requestDto.getUrl());
        AudioSegment audioSegment = audioSegmentService.createAudioSegment(serviceDto);
        return new ResponseEntity<>(new AudioSegmentRespDto(audioSegment), HttpStatus.CREATED);
    }

    @Operation(summary = "스피치 정보 업데이트 API")
    @ApiResponse(responseCode = "200", description = "업데이트 완료",
        content = @Content(schema = @Schema(implementation = SpeechRespDto.class)))
    @PatchMapping("/{speech-id}")
    public ResponseEntity<SpeechRespDto> updateSpeech(
        @Parameter(name = "presentation-id")
        @PathVariable(name = "presentation-id") Integer presentationId,
        @Parameter(name = "speech-id")
        @PathVariable(name = "speech-id") Integer speechId,
        @RequestBody SpeechUpdateReqDto requestDto) {
        SpeechUpdateDto serviceDto = new SpeechUpdateDto(
            presentationId, speechId, requestDto.getUserSymbol(), requestDto.getBookmarked()
        );

        Speech updatedSpeech = speechService.partialUpdate(serviceDto);

        return new ResponseEntity<>(new SpeechRespDto(updatedSpeech), HttpStatus.OK);
    }

    @Operation(
        summary = "분석 결과 조회용 presigned URL API"
    )
    @ApiResponse(responseCode = "200", description = "조회 성공")
    @ApiResponse(responseCode = "202", description = "아직 처리중인 경우",
        content = @Content)
    @ApiResponse(responseCode = "404", description = "전달된 ID를 가지는 스피치가 존재하지 않는 경우",
        content = @Content)
    @ApiResponse(responseCode = "409", description = "연습 종료 처리 되지 않은 스피치인 경우",
        content = @Content)
    @GetMapping("/{speech-id}/analysis-records")
    public ResponseEntity<Map<AnalysisType, URL>> getAnalysisRecords(
        @Parameter(name = "presentation-id")
        @PathVariable(name = "presentation-id") Integer presentationId,
        @Parameter(name = "speech-id")
        @PathVariable(name = "speech-id") Integer speechId) {
        Speech speech = speechService.findSpeechByIdAndPresentationId(
            speechId, presentationId, false
        );

        if (!speech.getRecordDone()) {
            throw new ConflictException("Record not done.");
        }

        Map<AnalysisType, URL> records = analysisResultService
            .getAnalysisRecordsOf(speech);

        // 누락 확인
        for (AnalysisType type : AnalysisType.values()) {
            if (!records.containsKey(type)) {
                return new ResponseEntity<>(HttpStatus.ACCEPTED);
            }
        }

        // Presign
        for (AnalysisType type : records.keySet()) {
            URL url = records.get(type);
            URL presignedUrl = presignedURLService.getPresignedURLForDownload(url);
            records.put(type, presignedUrl);
        }

        return new ResponseEntity<>(records, HttpStatus.OK);
    }

    @Hidden
    @PostMapping("/{speech-id}/clova-result-callback")
    public void clovaResultCallback(
        @PathVariable(name = "presentation-id") Integer presentationId,
        @PathVariable(name = "speech-id") Integer speechId,
        @RequestBody String body) {
        Speech speech = speechService.findSpeechByIdAndPresentationId(
            speechId, presentationId, false
        );

        // S3에 결과 업로드
        String key = speech.getPresentation().getId() + "/" + speech.getId() + "/analysis/STT.json";
        String strUrl = s3Service.uploadAsJson(key, body);

        // 논리적 피드백 준비
        try {
            Map<String, Object> sttObject = jsonParser.parseMap(body);
            String textScript = (String) sttObject.get("text");
            AIChatLog log = speechService.initChatGPTPrompt(speech, textScript);
        } catch (JsonParseException ex) {
            throw new UnhandledException(HttpStatus.INTERNAL_SERVER_ERROR,
                "Invalid JSON format in speech with id :" + speech.getId());
        }

        // 2차 분석 요청
        speechService.sttDoneAndStartAnalyze2(speech);
    }

    @Operation(
        summary = "스피치 삭제 API"
    )
    @ApiResponse(responseCode = "204", description = "스피치 삭제 성공",
        content = @Content)
    @ApiResponse(responseCode = "404", description = "전달된 id를 가지는 스피치가 존재하지 않는 경우",
        content = @Content)
    @DeleteMapping("/{speech-id}")
    public ResponseEntity<Void> deleteSpeech(
        @PathVariable(name = "presentation-id") Integer presentationId,
        @PathVariable(name = "speech-id") Integer speechId) {
        speechService.deleteById(speechId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @Operation(
        summary = "논리적 피드백 기록 목록 조회 API"
    )
    @ApiResponse(responseCode = "200", description = "조회 성공",
        content = @Content(schema = @Schema(implementation = AIChatLogListRespDto.class)))
    @ApiResponse(responseCode = "202", description = "논리적 피드백 준비가 아직 되지 않은 경우 (polling 필요)",
        content = @Content)
    @ApiResponse(responseCode = "404", description = "전달된 id를 가지는 스피치가 존재하지 않는 경우",
        content = @Content)
    @ApiResponse(responseCode = "409", description = "아직 연습이 끝나지 않은 스피치인 경우",
        content = @Content)
    @GetMapping("/{speech-id}/ai-chat-logs")
    public ResponseEntity<AIChatLogListRespDto> getAIChatLogs(
        @Parameter(name = "presentation-id")
        @PathVariable(name = "presentation-id") Integer presentationId,
        @Parameter(name = "speech-id")
        @PathVariable(name = "speech-id") Integer speechId) {
        Speech speech = speechService.findSpeechByIdAndPresentationId(
            speechId, presentationId, true);

        // 녹음이 끝나지 않은 경우 녹음 완료 처리 먼저 해야 함
        if (!speech.getRecordDone()) {
            throw new ConflictException("Record not done.");
        }

        AIChatLogRetrieveResult result = openAIService.getAIChatLogsOf(speech);
        List<AIChatLog> completedLogs = result.getCompletedLogs();
        List<AIChatLog> uncompletedLogs = result.getUncompletedLogs();
        List<AIChatLog> systemLogs = result.getSystemLogs();

        // 아직 초기화가 되지 않은 경우 폴링 필요
        if (!speech.getReadyToChat()) {
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        }

        AIChatLogListRespDto respDto = AIChatLogListRespDto.builder()
            .completedChatLogs(completedLogs.stream().map(AIChatLogRespDto::new).toList())
            .uncompletedChatLogs(uncompletedLogs.stream().map(AIChatLogRespDto::new).toList())
            .build();

        return new ResponseEntity<>(respDto, HttpStatus.OK);
    }

    @Operation(
        summary = "논리적 피드백 기록 단건 조회 API"
    )
    @ApiResponse(responseCode = "200", description = "조회 성공",
        content = @Content(schema = @Schema(implementation = AIChatLogListRespDto.class)))
    @ApiResponse(responseCode = "202", description = "처리 중인 경우 (polling 필요)",
        content = @Content)
    @ApiResponse(responseCode = "404", description = "전달된 id를 가지는 스피치가 존재하지 않는 경우",
        content = @Content)
    @ApiResponse(responseCode = "409", description = "아직 연습이 끝나지 않은 스피치인 경우",
        content = @Content)
    @GetMapping("/{speech-id}/ai-chat-logs/{log-id}")
    public ResponseEntity<AIChatLogRespDto> getAIChatLog(
        @Parameter(name = "presentation-id")
        @PathVariable(name = "presentation-id") Integer presentationId,
        @Parameter(name = "speech-id")
        @PathVariable(name = "speech-id") Integer speechId,
        @Parameter(name = "log-id")
        @PathVariable(name = "log-id") Long logId
    ) {
        Speech speech = speechService.findSpeechByIdAndPresentationId(
            speechId, presentationId, true);

        if (!speech.getRecordDone()) {
            throw new ConflictException("Record not done.");
        }

        AIChatLog log = openAIService.getAIChatLogById(logId);
        if (!log.getIsDone()) {
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>(new AIChatLogRespDto(log), HttpStatus.OK);
        }
    }

    @Operation(
        summary = "논리적 피드백 요청 API",
        description = "논리적 피드백 생성을 비동기적으로 요청하는 API로, 요청이 완료되면 202 상태코드를 반환한다. <br/>"
            + "응답으로 반환된 논리적 피드백의 id로 단건 조회 요청을 하여 논리적 피드백 완료 여부를 확인할 수 있다."
    )
    @ApiResponse(responseCode = "202", description = "생성 완료 (polling 필요)",
        content = @Content)
    @ApiResponse(responseCode = "404", description = "전달된 id를 가지는 스피치가 존재하지 않는 경우",
        content = @Content)
    @ApiResponse(responseCode = "409", description = "아직 연습이 끝나지 않은 스피치인 경우",
        content = @Content)
    @PostMapping("/{speech-id}/ai-chat-logs")
    public ResponseEntity<AIChatLogCreateResDto> createAIChatLog(
        @Parameter(name = "presentation-id")
        @PathVariable(name = "presentation-id") Integer presentationId,
        @Parameter(name = "speech-id")
        @PathVariable(name = "speech-id") Integer speechId,
        @RequestBody AIChatLogReqDto requestDto) {
        Speech speech = speechService.findSpeechByIdAndPresentationId(
            speechId, presentationId, false);

        if (!speech.getRecordDone()) {
            throw new ConflictException("Record not done.");
        }

        AIChatLog log = speechService.chatGPTPrompt(speech, requestDto.getPrompt());

        return new ResponseEntity<>(new AIChatLogCreateResDto(log), HttpStatus.ACCEPTED);
    }
}
