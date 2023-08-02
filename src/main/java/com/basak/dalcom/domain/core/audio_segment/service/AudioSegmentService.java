package com.basak.dalcom.domain.core.audio_segment.service;

import static com.basak.dalcom.aws.s3.S3Service.getKeyFromUrl;

import com.basak.dalcom.aws.s3.S3Service;
import com.basak.dalcom.domain.common.exception.stereotypes.NotFoundException;
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
    private final S3Service s3Service;

    public AudioSegment createAudioSegment(CreateAudioSegmentDto dto) {
        Speech dummySpeech = Speech.builder().id(dto.getSpeechId()).build();

        AudioSegment audioSegment = AudioSegment.builder()
            .speech(dummySpeech)
            .fullAudioS3Url(dto.getUrl().toString())
            .build();
        audioSegmentRepository.save(audioSegment);

        return audioSegment;
    }

    public void deleteAudioSegment(Long audioSegmentId) {
        AudioSegment audioSegment = audioSegmentRepository.findById(audioSegmentId)
            .orElseThrow(() -> new NotFoundException("AudioSegment"));
        String url = audioSegment.getFullAudioS3Url();
        String key = getKeyFromUrl(url);
        s3Service.deleteByKey(key);

        audioSegmentRepository.delete(audioSegment);
    }
}
