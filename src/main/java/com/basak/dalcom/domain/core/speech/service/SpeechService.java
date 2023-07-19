package com.basak.dalcom.domain.core.speech.service;

import com.basak.dalcom.aws.s3.presigned_url.PresignedURLService;
import com.basak.dalcom.domain.common.exception.HandledException;
import com.basak.dalcom.domain.core.presentation.data.Presentation;
import com.basak.dalcom.domain.core.presentation.data.PresentationRepository;
import com.basak.dalcom.domain.core.speech.data.Speech;
import com.basak.dalcom.domain.core.speech.data.SpeechRepository;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class SpeechService {

    private final SpeechRepository speechRepository;
    private final PresentationRepository presentationRepository;
    private final PresignedURLService presignedURLService;


    /**
     * Speech 녹음 완료 후 분석 시작하는 서비스
     */
    public void speechRecordDoneAndStartAnalyze(Integer speechId) {
        // TODO: Speech 음성 하나로 합치고 S3 URL 받기
        String FULL_S3_URL_DUMMY = "https://s3.ap-northeast-2.amazonaws.com/FULL_S3_URL_HERE.mp3";

        // TODO: Naver Clova에 음성 파일 보내기

        // TODO: Wasak 서버에 분석 보내기
    }


    /**
     * 최초 녹음 시작 시에 단일 Speech 생성하는 서비스
     */
    public Speech createSpeech(Integer presentationId) {
        Presentation targetPresentation = presentationRepository.findPresentationById(
            presentationId);

        Speech speech = Speech.builder()
            .presentation(targetPresentation)
            .build();

        speechRepository.save(speech);

        return speech;
    }

    /**
     * 단일 Speech 조회
     */
    public Speech findSpeechById(Integer speechId) {
        return speechRepository.findSpeechById(speechId)
            .orElseThrow(() -> new HandledException(HttpStatus.NOT_FOUND, "Speech not found."));
    }

    public Speech findSpeechByIdAndPresentationId(Integer speechId, Integer presentationId) {
        return speechRepository.findSpeechByIdAndPresentationId(speechId, presentationId)
            .orElseThrow(() -> new HandledException(HttpStatus.NOT_FOUND, "Speech not found."));
    }

    public String getAudioSegmentUploadKey(String ext) {
        LocalDateTime now = LocalDateTime.now();
        UUID uuid = UUID.randomUUID();
        Long mill = now.getNano() / 1000000L;
        return mill + "_" + uuid + "." + ext;
    }

    /**
     * Clova STT 분석 결과 수신해서 DB 저장
     */
    public void saveClovaResultToDB(Integer speechId, String dto) {
        speechRepository.findById(speechId)
            .orElseThrow(() -> new HandledException(HttpStatus.NOT_FOUND, "Speech not found."));

        speechRepository.updateSttScriptById(speechId, dto);
    }

    public URL getAudioSegmentUploadUrl(String key) {
        return presignedURLService.getPresignedURLForUpload(key);
    }

    public List<Speech> findSpeechesByPresentationId(Integer presentationId) {
        return speechRepository.findSpeechesByPresentationId(presentationId);
    }
}
