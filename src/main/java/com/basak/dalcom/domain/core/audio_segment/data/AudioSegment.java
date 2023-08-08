package com.basak.dalcom.domain.core.audio_segment.data;

import com.basak.dalcom.domain.common.BaseEntity;
import com.basak.dalcom.domain.core.speech.data.Speech;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Table(name = "audio_segment")
public class AudioSegment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne
    @JoinColumn(name = "speech_id", nullable = false)
    private Speech speech;

    @Column(nullable = false, unique = true)
    private String fullAudioS3Url;

    @Transient
    private LocalDateTime lastModifiedDate;

    public void updateAsPresignedUrl(String presignedUrl) {
        this.fullAudioS3Url = presignedUrl;
    }

    public void disconnectWithSpeech() {
        this.speech = null;
    }
}
