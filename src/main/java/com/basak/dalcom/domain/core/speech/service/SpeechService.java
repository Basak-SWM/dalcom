package com.basak.dalcom.domain.core.speech.service;

import com.basak.dalcom.domain.core.presentation.data.Presentation;
import com.basak.dalcom.domain.core.presentation.data.PresentationRepository;
import com.basak.dalcom.domain.core.speech.controller.request_dto.SpeechCreateDto;
import com.basak.dalcom.domain.core.speech.controller.request_dto.SpeechRecordDoneDto;
import com.basak.dalcom.domain.core.speech.data.Speech;
import com.basak.dalcom.domain.core.speech.data.SpeechRepository;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class SpeechService {

    private final SpeechRepository speechRepository;
    private final PresentationRepository presentationRepository;

    public void speechRecordDoneAndStartAnalyze (SpeechRecordDoneDto dto) {
        Presentation targetPresentation = presentationRepository.findPresentationById(dto.getPresentationId());

        // TODO: Speech 음성 하나로 합치고 S3 URL 받기
        String FULL_S3_URL_DUMMY = "https://s3.ap-northeast-2.amazonaws.com/FULL_S3_URL_HERE.mp3"

        // TODO: Naver Clova에 음성 파일 보내기

        // TODO: Wasak 서버에 분석 보내기
    }

    public Speech createSpeech(SpeechCreateDto dto
        Presentation targetPresentation = presentationRepository.findPresentationById(dto.getPresentationId());

        Speech speech = Speech.builder()
            .presentation(targetPresentation)
            .fullAudioS3Url(dto.getFullAudioS3Url())
            .build();

        speechRepository.save(speech);

        return speech;

    public Optional<Speech> findSpeechById(Integer presentationId, Integer speechId) {
        return speechRepository.findSpeechByPresentationAndId(presentationId, speechId);
    }

}
