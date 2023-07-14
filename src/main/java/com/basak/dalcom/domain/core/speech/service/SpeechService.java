package com.basak.dalcom.domain.core.speech.service;

import com.basak.dalcom.domain.core.speech.data.Speech;
import com.basak.dalcom.domain.core.speech.data.SpeechRepository;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class SpeechService {

    private final SpeechRepository speechRepository;

    public Optional<Speech> findSpeechById(Integer presentationId, Integer speechId) {
        return speechRepository.findSpeechByPresentationAndId(presentationId, speechId);
    }
}
