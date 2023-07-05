package com.basak.dalcom.domain.core;

import com.basak.dalcom.domain.common.CreatedAtAudit;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "audio_segment")
public class AudioSegment extends CreatedAtAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "speech_id", nullable = false)
    private Speech speech;

    @Column(nullable = false)
    private String fullAudioS3Url;

    @Column(nullable = false)
    private LocalDateTime uploadedAt;
}
