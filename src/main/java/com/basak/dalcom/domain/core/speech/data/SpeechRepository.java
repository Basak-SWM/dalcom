package com.basak.dalcom.domain.core.speech.data;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpeechRepository extends JpaRepository<Speech, Integer> {

    Optional<Speech> findSpeechById(Integer speechId);

    Optional<Speech> findSpeechByIdAndPresentationId(Integer speechId, Integer presentationId);

    List<Speech> findSpeechesByPresentationId(Integer presentationId);

    boolean existsByIdAndPresentationId(Integer speechId, Integer presentationId);
}
