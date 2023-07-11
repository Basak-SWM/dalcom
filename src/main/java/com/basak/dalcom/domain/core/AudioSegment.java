package com.basak.dalcom.domain.core;

import com.basak.dalcom.domain.common.BaseEntity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "audio_segment")
public class AudioSegment extends BaseEntity {
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

    @Transient
    private LocalDateTime lastModifiedDate;
}
