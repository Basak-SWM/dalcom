package com.basak.dalcom.domain.core.analysis_record.data;

import com.basak.dalcom.domain.core.speech.data.Speech;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnalysisRecordRepository extends JpaRepository<AnalysisRecord, Long> {

    List<AnalysisRecord> findAllBySpeech(Speech speech);
}
