package com.basak.dalcom.domain.core.audio_segment.service;

import com.basak.dalcom.domain.core.audio_segment.data.AudioSegment;
import com.basak.dalcom.domain.core.audio_segment.data.AudioSegmentRepository;
import com.basak.dalcom.domain.core.speech.data.Speech;
import com.basak.dalcom.domain.core.speech.service.SpeechService;
import java.net.URL;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class AudioSegmentService {

    private final AudioSegmentRepository audioSegmentRepository;
    private final SpeechService speechService;

    public AudioSegment createAudioSegment(Integer speechId, URL url) {
        Speech speech = speechService.findSpeechById(speechId);

        AudioSegment audioSegment = AudioSegment.builder()
            .speech(speech)
            .fullAudioS3Url(url.toString())
            .build();
        audioSegmentRepository.save(audioSegment);

        return audioSegment;
    }
}
