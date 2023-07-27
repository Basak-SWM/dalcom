package com.basak.dalcom.domain.core.analysis_result.data;

import java.net.URL;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class AnalysisResult {

    URL presignedURL;
    AnalysisType type;
}
