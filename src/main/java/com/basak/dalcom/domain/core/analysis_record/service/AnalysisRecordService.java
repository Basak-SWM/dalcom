package com.basak.dalcom.domain.core.analysis_record.service;

import com.basak.dalcom.domain.common.exception.stereotypes.ConflictException;
import com.basak.dalcom.domain.core.analysis_record.data.AnalysisRecord;
import com.basak.dalcom.domain.core.analysis_record.data.AnalysisRecordRepository;
import com.basak.dalcom.domain.core.analysis_record.data.AnalysisRecordType;
import com.basak.dalcom.domain.core.speech.data.Speech;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class AnalysisRecordService {

    private final AnalysisRecordRepository analysisRecordRepository;

    public Map<AnalysisRecordType, Boolean> getAnalysisRecordsOf(Speech speech) {
        if (!speech.getRecordDone()) {
            throw new ConflictException("Record is not done yet");
        }

        Map<AnalysisRecordType, Boolean> result = new HashMap<>();

        for (AnalysisRecordType type : AnalysisRecordType.values()) {
            result.put(type, false);
        }

        List<AnalysisRecord> records = analysisRecordRepository.findAllBySpeech(speech);
        for (AnalysisRecord record : records) {
            result.put(record.getType(), true);
        }

        return result;
    }

    public AnalysisRecord createAnalysisRecordOf(Speech speech, AnalysisRecordType type) {
        AnalysisRecord record = AnalysisRecord.builder()
            .speech(speech)
            .type(type)
            .build();
        return analysisRecordRepository.save(record);
    }
}
