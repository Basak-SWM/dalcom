package com.basak.dalcom.domain.core.analysis_result.data;

import com.basak.dalcom.aws.s3.presigned_url.PresignedURLService;
import com.basak.dalcom.domain.core.speech.data.Speech;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

@AllArgsConstructor
@Repository
public class AnalysisResultRepositoryImpl implements AnalysisResultRepository {

    private final PresignedURLService presignedURLService;

    @Override
    public List<AnalysisResult> getAnalysisResultsOf(Speech speech) {
        List<AnalysisResult> resultList = new ArrayList<>();

        for (AnalysisType type : AnalysisType.values()) {
            AnalysisResult result = getAnalysisResultOf(speech, type);
            resultList.add(result);
        }

        return resultList;
    }

    @Override
    public AnalysisResult getAnalysisResultOf(Speech speech, AnalysisType type) {
        String prefix = getPrefixOf(speech);
        String key = String.format("%s/%s.json", prefix, type.getValue());
        URL presignedURL = presignedURLService.getPresignedURLForDownload(key);

        return AnalysisResult.builder()
            .type(type)
            .presignedURL(presignedURL)
            .build();
    }

    private String getPrefixOf(Speech speech) {
        return String.format("%d/%d/analysis", speech.getPresentation().getId(), speech.getId());
    }

    @Override
    public void save(Speech speech, AnalysisType type, String body) {

    }
}
