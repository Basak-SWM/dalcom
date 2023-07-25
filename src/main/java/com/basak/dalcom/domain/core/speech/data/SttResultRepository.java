package com.basak.dalcom.domain.core.speech.data;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SttResultRepository extends JpaRepository<SttResult, Integer> {

    Optional<SttResult> findBySpeech(Speech speech);
}
