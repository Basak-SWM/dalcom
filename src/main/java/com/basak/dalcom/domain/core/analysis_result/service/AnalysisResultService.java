package com.basak.dalcom.domain.core.analysis_result.service;

import com.basak.dalcom.domain.core.analysis_result.data.AnalysisResult;
import com.basak.dalcom.domain.core.analysis_result.data.AnalysisResultRepository;
import com.basak.dalcom.domain.core.analysis_result.data.AnalysisType;
import com.basak.dalcom.domain.core.speech.data.Speech;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class AnalysisResultService {

    private final AnalysisResultRepository analysisResultRepository;

    public Map<AnalysisType, URL> getAnalysisResultsOf(Speech speech) {
        List<AnalysisResult> resultList = analysisResultRepository.getAnalysisResultsOf(speech);

        Map<AnalysisType, URL> resultMap = new HashMap<>();
        for (AnalysisResult result : resultList) {
            resultMap.put(result.getType(), result.getPresignedURL());
        }

        return resultMap;
    }

    public void createAnalysisResultOf(Speech speech, AnalysisType type, String body) {
        analysisResultRepository.save(speech, type, body);
    }
}
