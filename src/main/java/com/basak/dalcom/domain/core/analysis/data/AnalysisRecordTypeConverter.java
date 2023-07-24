package com.basak.dalcom.domain.core.analysis.data;

import javax.persistence.AttributeConverter;

public class AnalysisRecordTypeConverter implements AttributeConverter<AnalysisRecordType, String> {

    @Override
    public String convertToDatabaseColumn(AnalysisRecordType attribute) {
        return AnalysisRecordType.valueOf(attribute)
            .orElseThrow(() -> new IllegalStateException("Unknown AnalysisRecordType"));
    }

    @Override
    public AnalysisRecordType convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }

        return AnalysisRecordType.enumOf(dbData)
            .orElseThrow(() -> new IllegalStateException("Unknown AnalysisRecordType Value"));
    }
}
