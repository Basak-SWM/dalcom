package com.basak.dalcom.domain.core.speech.data;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpeechRepository extends JpaRepository<Speech, Integer> {
    Optional<Speech> findSpeechByPresentationAndId(Integer presentationId, Integer speechId);
}
