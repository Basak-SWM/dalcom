package com.basak.dalcom.domain.core.speech.data;

import com.basak.dalcom.domain.common.BaseEntity;
import com.basak.dalcom.domain.core.analysis.data.AnalysisRecord;
import com.basak.dalcom.domain.core.audio_segment.data.AudioSegment;
import com.basak.dalcom.domain.core.presentation.data.Presentation;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    private String fullAudioS3Url;

    @Setter
    @Column(columnDefinition = "MEDIUMTEXT")
    private String userSymbol;

    @Setter
    @OneToMany(mappedBy = "speech", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AudioSegment> audioSegments;

    @OneToMany(mappedBy = "speech", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AnalysisRecord> analysisRecords;

    @OneToOne(mappedBy = "speech")
    private SttResult sttResult;

    public void setPresignedAudioSegments(List<AudioSegment> presignedAudioSegments) {
        this.audioSegments = presignedAudioSegments;
    }
}
