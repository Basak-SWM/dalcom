package com.basak.dalcom.domain.core.speech.data;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AIChatLogRepository extends JpaRepository<AIChatLog, Long> {

    List<AIChatLog> findAllBySpeech(Speech speech);
}
