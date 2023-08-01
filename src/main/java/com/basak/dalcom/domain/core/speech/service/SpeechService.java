package com.basak.dalcom.domain.core.speech.service;

import com.basak.dalcom.aws.s3.presigned_url.PresignedURLService;
import com.basak.dalcom.config.NCPConfig;
import com.basak.dalcom.domain.common.exception.stereotypes.NotFoundException;
import com.basak.dalcom.domain.core.presentation.data.Presentation;
import com.basak.dalcom.domain.core.presentation.data.PresentationRepository;
import com.basak.dalcom.domain.core.speech.data.Speech;
import com.basak.dalcom.domain.core.speech.data.SpeechRepository;
import com.basak.dalcom.domain.core.speech.service.dto.SpeechUpdateDto;
import com.basak.dalcom.external_api.wasak.service.WasakService;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
    public void speechRecordDoneAndStartAnalyze(Speech speech) {
        URL callbackUrl = ncpConfig.getClovaSpeechCallbackUrl(
            speech.getPresentation().getId(), speech.getId()
        );

        String fullAudioKey = getFullAudioUploadKey(
            speech.getPresentation().getId(), speech.getId(), "mp3"
        );
        URL downloadUrl = presignedURLService.getPresignedURLForDownload(fullAudioKey);

        wasakService.requestAnalysis1(speech.getId(), callbackUrl, fullAudioKey, downloadUrl);

        speech.setRecordDone();
    }

    /**
     * STT 처리 완료 시그널 발생시 이를 음성 분석 서버에 전송하는 서비스
     */
    public void sttDoneAndStartAnalyze2(Speech speech) {
        wasakService.requestAnalysis2(speech.getPresentation().getId(), speech.getId());
    }


    /**
     * 최초 녹음 시작 시에 단일 Speech 생성하는 서비스
     */
    public Speech createSpeech(Integer presentationId) {
        Presentation targetPresentation = presentationRepository
            .findPresentationById(presentationId)
            .orElseThrow(() -> new NotFoundException("Presentation"));

        Speech speech = Speech.builder()
            .presentation(targetPresentation)
            .recordDone(false)
            .build();

        speechRepository.save(speech);

        return speech;
    }

    /**
     * 단일 Speech 조회
     */
    public Speech findSpeechById(Integer speechId) {
        return speechRepository.findSpeechById(speechId)
            .orElseThrow(() -> new NotFoundException("Speech"));
    }

    public Speech findSpeechByIdAndPresentationId(
        Integer speechId, Integer presentationId, boolean withPresignedAudioSegments) {
        Speech speech = speechRepository.findSpeechByIdAndPresentationId(speechId, presentationId)
            .orElseThrow(() -> new NotFoundException("Speech"));

//        speech.setPresignedAudioSegments(speech.getAudioSegments().stream()
//            .sorted(Comparator.comparing(AudioSegment::getFullAudioS3Url)).toList());
//
//        if (withPresignedAudioSegments) {
//            List<AudioSegment> audioSegments = speech.getAudioSegments();
//
//            for (AudioSegment audioSegment : audioSegments) {
//                try {
//                    URL presignedUrl = presignedURLService.getPresignedURLForDownload(
//                        new URL(audioSegment.getFullAudioS3Url()));
//                    audioSegment.updateAsPresignedUrl(presignedUrl.toString());
//                } catch (MalformedURLException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//
//            speech.setPresignedAudioSegments(audioSegments);
//        }

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


    public URL getAudioSegmentUploadUrl(String key) {
        return presignedURLService.getPresignedURLForUpload(key);
    }

    public List<Speech> findSpeechesByPresentationId(Integer presentationId) {
        return speechRepository.findSpeechesByPresentationId(presentationId);
    }

    public void checkExistence(Integer presentationId, Integer speechId) {
        if (!speechRepository.existsByIdAndPresentationId(speechId, presentationId)) {
            throw new NotFoundException("Speech");
        }
    }

    @Transactional
    public Speech partialUpdate(SpeechUpdateDto dto) {
        Speech speech = speechRepository.findSpeechByIdAndPresentationId(
            dto.getSpeechId(), dto.getPresentationId()
        ).orElseThrow(() -> new NotFoundException("Speech"));

        dto.getUserSymbol().ifPresent(userSymbol -> speech.setUserSymbol(
            dto.getUserSymbol().get()
        ));

        return speech;
    }
}
