package com.basak.dalcom.domain.core.speech.service;

import com.basak.dalcom.aws.s3.presigned_url.PresignedURLService;
import com.basak.dalcom.domain.common.exception.HandledException;
import com.basak.dalcom.domain.core.audio_segment.data.AudioSegment;
import com.basak.dalcom.domain.core.audio_segment.service.AudioSegmentService;
import com.basak.dalcom.domain.core.presentation.data.Presentation;
import com.basak.dalcom.domain.core.presentation.data.PresentationRepository;
import com.basak.dalcom.domain.core.speech.data.Speech;
import com.basak.dalcom.domain.core.speech.data.SpeechRepository;
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
    private final AudioSegmentService audioSegmentService;


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

    public Speech findSpeechByIdAndPresentationId(Integer speechId, Integer presentationId,
        boolean withAudioSegments) {
        Speech speech = speechRepository.findSpeechByIdAndPresentationId(speechId, presentationId)
            .orElseThrow(() -> new HandledException(HttpStatus.NOT_FOUND, "Speech not found."));

        speech.setPresignedAudioSegments(speech.getAudioSegments().stream()
            .sorted(Comparator.comparing(AudioSegment::getFullAudioS3Url)).toList());

        if (withAudioSegments) {
            List<AudioSegment> audioSegments = speech.getAudioSegments();

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
        return presentationId + "/" + speechId + "/" + timestamp + "_" + uuid + "." + ext;
    }

    public URL getAudioSegmentUploadUrl(String key) {
        return presignedURLService.getPresignedURLForUpload(key);
    }

    public List<Speech> findSpeechesByPresentationId(Integer presentationId) {
        return speechRepository.findSpeechesByPresentationId(presentationId);
    }
}
