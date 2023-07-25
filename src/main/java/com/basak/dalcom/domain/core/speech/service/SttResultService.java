package com.basak.dalcom.domain.core.speech.service;


import com.basak.dalcom.domain.common.exception.stereotypes.ConflictException;
import com.basak.dalcom.domain.core.analysis_record.data.AnalysisRecordType;
import com.basak.dalcom.domain.core.analysis_record.service.AnalysisRecordService;
import com.basak.dalcom.domain.core.speech.data.Speech;
import com.basak.dalcom.domain.core.speech.data.SttResult;
import com.basak.dalcom.domain.core.speech.data.SttResultRepository;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class SttResultService {

    private final SttResultRepository sttResultRepository;
    private final SpeechService speechService;
    private final AnalysisRecordService analysisRecordService;

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
        analysisRecordService.createAnalysisRecordOf(speech, AnalysisRecordType.STT);

        return sttResult;
    }

    public Optional<SttResult> getSttResultOf(Speech speech) {
        if (!speech.getRecordDone()) {
            throw new ConflictException("Record is not done yet.");
        } else {
            return sttResultRepository.findBySpeech(speech);
        }
    }
}
