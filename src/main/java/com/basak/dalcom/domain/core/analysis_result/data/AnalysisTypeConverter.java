package com.basak.dalcom.domain.core.analysis_result.data;

import javax.persistence.AttributeConverter;

public class AnalysisTypeConverter implements AttributeConverter<AnalysisType, String> {

    @Override
    public String convertToDatabaseColumn(AnalysisType attribute) {
        return AnalysisType.valueOf(attribute)
            .orElseThrow(() -> new IllegalStateException("Unknown AnalysisType"));
    }

    @Override
    public AnalysisType convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }

        return AnalysisType.enumOf(dbData)
            .orElseThrow(() -> new IllegalStateException("Unknown AnalysisType Value"));
    }
}
