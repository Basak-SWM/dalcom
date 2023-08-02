package com.basak.dalcom.domain.core.speech.data;

import com.basak.dalcom.domain.common.BaseEntity;
import com.basak.dalcom.domain.core.audio_segment.data.AudioSegment;
import com.basak.dalcom.domain.core.presentation.data.Presentation;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "speech")
public class Speech extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "presentation_id", nullable = false)
    private Presentation presentation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reference_speech_id")
    private Speech referenceSpeech;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "referenceSpeech")
    private List<Speech> referencingSpeeches;

    private String fullAudioS3Url;

    @Column(nullable = false)
    @ColumnDefault("0")
    private Boolean recordDone;

    @Setter
    @Column(columnDefinition = "MEDIUMTEXT")
    private String userSymbol;

    @Setter
    @OneToMany(mappedBy = "speech", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AudioSegment> audioSegments;

    public void setRecordDone() {
        this.recordDone = true;
    }

    public void disconnectReferenceSpeech() {
        this.referenceSpeech = null;
    }
}
