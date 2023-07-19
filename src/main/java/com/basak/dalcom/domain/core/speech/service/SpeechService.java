package com.basak.dalcom.domain.core.speech.service;

import com.basak.dalcom.aws.s3.presigned_url.PresignedURLService;
import com.basak.dalcom.config.NCPConfig;
import com.basak.dalcom.domain.common.exception.HandledException;
import com.basak.dalcom.domain.core.audio_segment.data.AudioSegment;
import com.basak.dalcom.domain.core.presentation.data.Presentation;
import com.basak.dalcom.domain.core.presentation.data.PresentationRepository;
import com.basak.dalcom.domain.core.speech.data.Speech;
import com.basak.dalcom.domain.core.speech.data.SpeechRepository;
import com.basak.dalcom.external_api.wasak.service.WasakService;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Comparator;
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
    private final WasakService wasakService;

    private final NCPConfig ncpConfig;

    /**
     * Speech 녹음 완료 후 분석 시작하는 서비스
     */
    public void speechRecordDoneAndStartAnalyze(Integer presentationId, Integer speechId) {
        URL callbackUrl = ncpConfig.getClovaSpeechCallbackUrl(presentationId, speechId);

        String fullAudioKey = getFullAudioUploadKey(presentationId, speechId, "mp3");
        URL uploadUrl = presignedURLService.getPresignedURLForUpload(fullAudioKey);
        URL downloadUrl = presignedURLService.getPresignedURLForDownload(fullAudioKey);

        wasakService.requestAnalysis1(speechId, callbackUrl, uploadUrl, downloadUrl);
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

    public Speech findSpeechByIdAndPresentationId(Integer speechId, Integer presentationId,
        boolean preign) {
        Speech speech = speechRepository.findSpeechByIdAndPresentationId(speechId, presentationId)
            .orElseThrow(() -> new HandledException(HttpStatus.NOT_FOUND, "Speech not found."));

        if (preign) {
            List<AudioSegment> audioSegments = speech.getAudioSegments().stream()
                .sorted(Comparator.comparing(AudioSegment::getFullAudioS3Url))
                .toList();

            for (AudioSegment audioSegment : audioSegments) {
                try {
                    URL presignedUrl = presignedURLService.getPresignedURLForDownload(
                        new URL(audioSegment.getFullAudioS3Url()));
                    audioSegment.updateAsPresignedUrl(presignedUrl.toString());
                } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                }
            }

            speech.setPresignedAudioSegments(audioSegments);
        }

        return speech;
    }

    public String getAudioSegmentUploadKey(Integer presentationId, Integer speechId, String ext) {
        LocalDateTime now = LocalDateTime.now();
        UUID uuid = UUID.randomUUID();
        Long timestamp = Timestamp.valueOf(now).getTime();
        return presentationId + "/" + speechId + "/audio_segments/" + timestamp + "_" + uuid + "."
            + ext;
    }

    public String getFullAudioUploadKey(Integer presentationId, Integer speechId, String ext) {
        return presentationId + "/" + speechId + "/full_audio." + ext;
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
