package com.basak.dalcom.domain.core.analysis_result.data;


import com.basak.dalcom.domain.core.speech.data.Speech;
import java.util.List;

public interface AnalysisResultRepository {

    List<AnalysisResult> getAnalysisResultsOf(Speech speech);

    AnalysisResult getAnalysisResultOf(Speech speech, AnalysisType type);

    void save(Speech speech, AnalysisType type, String body);
}
