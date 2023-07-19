package com.basak.dalcom.domain.core.audio_segment.data;

import com.basak.dalcom.domain.core.speech.data.Speech;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AudioSegmentRepository extends JpaRepository<AudioSegment, Long> {

    List<AudioSegment> findAudioSegmentsBySpeech(Speech speech);
}
