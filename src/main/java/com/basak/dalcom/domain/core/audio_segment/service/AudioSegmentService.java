package com.basak.dalcom.domain.core.audio_segment.service;

import com.basak.dalcom.domain.core.audio_segment.data.AudioSegment;
import com.basak.dalcom.domain.core.audio_segment.data.AudioSegmentRepository;
import com.basak.dalcom.domain.core.audio_segment.service.dto.CreateAudioSegmentDto;
import com.basak.dalcom.domain.core.speech.data.Speech;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class AudioSegmentService {

    private final AudioSegmentRepository audioSegmentRepository;

    public AudioSegment createAudioSegment(CreateAudioSegmentDto dto) {
        Speech dummySpeech = Speech.builder().id(dto.getSpeechId()).build();

        AudioSegment audioSegment = AudioSegment.builder()
            .speech(dummySpeech)
            .fullAudioS3Url(dto.getUrl().toString())
            .build();
        audioSegmentRepository.save(audioSegment);

        return audioSegment;
    }
}
