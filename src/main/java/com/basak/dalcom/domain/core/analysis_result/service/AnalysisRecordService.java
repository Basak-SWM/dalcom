package com.basak.dalcom.domain.core.analysis_result.service;

import com.basak.dalcom.domain.common.exception.UnhandledException;
import com.basak.dalcom.domain.core.analysis_result.data.AnalysisRecord;
import com.basak.dalcom.domain.core.analysis_result.data.AnalysisRecordRepository;
import com.basak.dalcom.domain.core.analysis_result.data.AnalysisType;
import com.basak.dalcom.domain.core.speech.data.Speech;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class AnalysisRecordService {

    private final AnalysisRecordRepository analysisRecordRepository;

    public Map<AnalysisType, URL> getAnalysisRecordsOf(Speech speech) {
        List<AnalysisRecord> resultList = speech.getAnalysisRecords();

        Map<AnalysisType, URL> resultMap = new HashMap<>();
        for (AnalysisRecord result : resultList) {
            try {
                resultMap.put(result.getType(), new URL(result.getUrl()));
            } catch (MalformedURLException e) {
                throw new UnhandledException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to initialize URL : " + result.getUrl()
                );
            }
        }

        return resultMap;
    }

    public void createAnalysisRecordOf(Speech speech, AnalysisType type, String strUrl) {
        AnalysisRecord record = AnalysisRecord.builder()
            .speech(speech)
            .type(type)
            .url(strUrl)
            .build();
        analysisRecordRepository.save(record);
    }

    public void deleteAnalysisRecords(List<AnalysisRecord> analysisRecords) {
        analysisRecordRepository.deleteAllInBatch(analysisRecords);
    }
}
