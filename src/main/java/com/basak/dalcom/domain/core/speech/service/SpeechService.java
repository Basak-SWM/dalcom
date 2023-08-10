package com.basak.dalcom.domain.core.speech.service;

import com.basak.dalcom.aws.s3.S3Service;
import com.basak.dalcom.aws.s3.presigned_url.PresignedURLService;
import com.basak.dalcom.config.NCPConfig;
import com.basak.dalcom.config.OpenAIConfig;
import com.basak.dalcom.domain.common.exception.stereotypes.ConflictException;
import com.basak.dalcom.domain.common.exception.stereotypes.NotFoundException;
import com.basak.dalcom.domain.core.analysis_result.data.AnalysisType;
import com.basak.dalcom.domain.core.analysis_result.service.AnalysisRecordService;
import com.basak.dalcom.domain.core.audio_segment.data.AudioSegment;
import com.basak.dalcom.domain.core.audio_segment.service.AudioSegmentService;
import com.basak.dalcom.domain.core.presentation.data.Presentation;
import com.basak.dalcom.domain.core.presentation.data.PresentationRepository;
import com.basak.dalcom.domain.core.speech.data.AIChatLog;
import com.basak.dalcom.domain.core.speech.data.Speech;
import com.basak.dalcom.domain.core.speech.data.SpeechRepository;
import com.basak.dalcom.domain.core.speech.service.dto.SpeechUpdateDto;
import com.basak.dalcom.external_api.openai.controller.dto.OpenAIRole;
import com.basak.dalcom.external_api.openai.service.OpenAIService;
import com.basak.dalcom.external_api.wasak.service.WasakService;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Service
public class SpeechService {

    private static final String[] ROLE_DESCRIPTION_PROMPT = {
        "You are an expert who helps student write better after receiving a script for a speech.",
        "You have to answer in Korean within 100 tokens.",
        "Evaluate the logic of the script mainly, and if you need additional information, it is okay to ask active questions to the student."
    };

    private final SpeechRepository speechRepository;
    private final PresentationRepository presentationRepository;
    private final AnalysisRecordService analysisRecordService;
    private final PresignedURLService presignedURLService;
    private final S3Service s3Service;
    private final WasakService wasakService;
    private final AudioSegmentService audioSegmentService;
    private final OpenAIService openAIService;
    private final NCPConfig ncpConfig;
    private final OpenAIConfig openAIConfig;

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

        wasakService.requestAnalysis1(speech.getPresentation().getId(), speech.getId(),
            callbackUrl, fullAudioKey, downloadUrl);

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
    @Transactional
    public Speech createSpeech(Integer presentationId, Optional<Integer> referenceSpeechId) {
        Presentation targetPresentation = presentationRepository
            .findPresentationById(presentationId)
            .orElseThrow(() -> new NotFoundException("Presentation"));

        Speech referenceSpeech = null;
        if (referenceSpeechId.isPresent()) {
            referenceSpeech = speechRepository
                .findSpeechById(referenceSpeechId.get())
                .orElseThrow(() -> new NotFoundException("Reference Speech"));
        }

        if (referenceSpeech != null && !referenceSpeech.getRecordDone()) {
            throw new ConflictException("Reference Speech is not record done");
        }

        targetPresentation.increaseSpeechAutoIncrementValue();

        Speech speech = Speech.builder()
            .presentation(targetPresentation)
            .recordDone(false)
            .readyToChat(false)
            .bookmarked(false)
            .referenceSpeech(referenceSpeech)
            .order(targetPresentation.getSpeechAutoIncrementValue())
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

        speech.setPresignedAudioSegments(speech.getAudioSegments().stream()
            .sorted(Comparator.comparing(AudioSegment::getFullAudioS3Url)).toList());

        if (withPresignedAudioSegments) {
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

            if (speech.getFullAudioS3Url() != null) {
                try {
                    speech.setFullAudioS3Url(presignedURLService.getPresignedURLForDownload(
                        new URL(speech.getFullAudioS3Url())).toString());
                } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                }
            }
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

        dto.getBookmarked().ifPresent(bookmarked -> speech.setBookmarked(
            dto.getBookmarked().get()
        ));

        return speech;
    }

    public void deleteById(Integer speechId) {
        Speech speech = speechRepository.findSpeechById(speechId)
            .orElseThrow(() -> new NotFoundException("Speech"));

        // 연결된 오디오 세그먼트 삭제
        List<AudioSegment> audioSegments = speech.getAudioSegments();
        audioSegmentService.deleteAudioSegments(audioSegments);

        // 연결된 분석 결과 삭제
        for (AnalysisType type : AnalysisType.values()) {
            String key = speech.getPresentation().getId() + "/" + speech.getId() + "/analysis/"
                + type.getValue() + ".json";
            s3Service.deleteByKey(key);
        }
        analysisRecordService.deleteAnalysisRecords(speech.getAnalysisRecords());

        // full audio 삭제
        String key = speech.getPresentation().getId() + "/" + speech.getId() + "/full_audio.mp3";
        s3Service.deleteByKey(key);

        // 참조 관계로 되어 있던 파생 스피치들과의 연결 해제
        speech.getReferencingSpeeches().forEach(referencingSpeech -> {
            referencingSpeech.disconnectReferenceSpeech();
        });

        // AI Chat Log 삭제
        List<AIChatLog> aiChatLogs = speech.getAiChatLogs();
        openAIService.deleteAIChatLogs(aiChatLogs);

        // 스피치 삭제
        speechRepository.deleteById(speechId);
    }

    public AIChatLog chatGPTPrompt(Speech speech, String prompt) {
        AIChatLog emptyAIChatLog = openAIService.createEmptyAIChatLog(
            speech, prompt, OpenAIRole.USER
        );
        openAIService.doAsyncPrompt(emptyAIChatLog);
        return emptyAIChatLog;
    }

    @Async
    public void initChatGPTPrompt(Speech speech, String textScript) {
        CompletableFuture
            .supplyAsync(() -> openAIService.doFirstSystemPrompt(
                speech, ROLE_DESCRIPTION_PROMPT, textScript)
            ).thenApply((aiChatLog) -> openAIService.doDefaultUserPrompt(aiChatLog))
            .thenAccept((readySpeech) -> updateSpeechReadyToChat(readySpeech));
    }

    @Transactional
    public void updateSpeechReadyToChat(Speech speech) {
//        speech.setReadyToChat();
//        speechRepository.save(speech);
        speechRepository.updateReadyToChatById(speech.getId(), true);
    }
}
