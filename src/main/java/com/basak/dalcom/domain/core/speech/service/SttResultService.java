package com.basak.dalcom.domain.core.speech.service;


import com.basak.dalcom.domain.core.speech.data.Speech;
import com.basak.dalcom.domain.core.speech.data.SttResult;
import com.basak.dalcom.domain.core.speech.data.SttResultRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class SttResultService {

    private final SttResultRepository sttResultRepository;
    private final SpeechService speechService;

    /**
     * Clova STT 분석 결과 수신해서 DB 저장
     */
    public SttResult createSttResult(Integer speechId, String body) {
        Speech speech = speechService.findSpeechById(speechId);
        SttResult sttResult = SttResult.builder()
            .speech(speech)
            .body(body)
            .build();

        sttResultRepository.save(sttResult);

        return sttResult;
    }
}
