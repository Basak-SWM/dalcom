package com.basak.dalcom.domain.core.analysis_result.controller;

import com.basak.dalcom.domain.common.exception.stereotypes.ConflictException;
import com.basak.dalcom.domain.core.analysis_result.service.AnalysisResultService;
import com.basak.dalcom.domain.core.speech.data.Speech;
import com.basak.dalcom.domain.core.speech.service.SpeechService;
import com.basak.dalcom.external_api.redis.repository.RedisAnalysisStatus;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/presentations/{presentation-id}/speeches/{speech-id}/analysis-result")
@RestController
public class AnalysisResultController {

    private final AnalysisResultService analysisResultService;

    private final SpeechService speechService;

    @GetMapping("")
    public ResponseEntity<Map<String, Object>> getAnalysisResult(
        @PathVariable(name = "presentation-id") Integer presentationId,
        @PathVariable(name = "speech-id") Integer speechId) {
        // 캐시에 있는지 확인
        Optional<RedisAnalysisStatus> cacheStatus = analysisResultService.getAnalysisStatus(
            speechId);

        // 캐시에 없으면
        if (!cacheStatus.isPresent()) {
            // DB에 저장되어 있는지 확인
            Optional<Map<String, Object>> result = analysisResultService.findById(speechId);
            // DB에 없으면 아직 분석 요청이 안됨
            if (!result.isPresent()) {
                log.info("{}) 캐시에도 없고, DB에도 없음", speechId);
                return new ResponseEntity(HttpStatus.NOT_FOUND);
            }
            // DB에 있으면 그대로 응답
            else {
                log.info("{}) 캐시에는 없고, DB에 있음", speechId);
                return new ResponseEntity<>(result.get(), HttpStatus.OK);
            }
        }

        log.info("{}) 캐시에 있음", speechId);
        // 캐시에 있으면
        // 다 완료될 때까지 대기
        for (int trial = 0; trial < 10; trial++) {
            log.info("{}) {}번째 시도", speechId, trial + 1);

            boolean allDone = true;
            boolean hasError = false;

            // 처리 상태 확인
            for (Map.Entry<String, RedisAnalysisStatus.Status> entry : cacheStatus.get()
                .getStatusMap().entrySet()) {
                if (entry.getValue() != RedisAnalysisStatus.Status.SUCCESS) {
                    allDone = false;
                }
                if (entry.getValue() == RedisAnalysisStatus.Status.ERROR) {
                    hasError = true;
                }
            }

            // 모두 완료되었으면 DB 조회
            if (allDone) {
                log.info("{}) 모두 완료됨", speechId);
                Optional<Map<String, Object>> result = analysisResultService.findById(speechId);
                return new ResponseEntity<>(result.get(), HttpStatus.OK);
            } else if (hasError) {
                log.info("{}) 에러 발생", speechId);
                return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
            } else {
                log.info("{}) 아직 완료되지 않음 : {}", speechId, cacheStatus.get());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        log.info("{}) 10번 시도해도 완료되지 않음", speechId);
        // 10번 시도해도 완료되지 않았으면 다시 폴링 필요
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }

    @PostMapping("")
    public ResponseEntity<Void> createAnalysisResult(
        @PathVariable(name = "presentation-id") Integer presentationId,
        @PathVariable(name = "speech-id") Integer speechId) {
        speechService.checkExistence(presentationId, speechId);

        Speech speech = speechService.findSpeechByIdAndPresentationId(
            speechId, presentationId, false
        );

        if (speech.getRecordDone()) {
            throw new ConflictException("Already record done.");
        }

        // TODO : 실제로 스타트 람다 요청하는 코드 필요

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
