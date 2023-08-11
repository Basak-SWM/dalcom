package com.basak.dalcom.domain.core.speech.data;

import com.basak.dalcom.domain.common.BaseEntity;
import com.basak.dalcom.domain.core.analysis_result.data.AnalysisRecord;
import com.basak.dalcom.domain.core.audio_segment.data.AudioSegment;
import com.basak.dalcom.domain.core.presentation.data.Presentation;
import java.util.List;
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
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
@Entity
@Table(name = "speech")
public class Speech extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne
    @JoinColumn(name = "presentation_id", nullable = false)
    private Presentation presentation;

    @OnDelete(action = OnDeleteAction.NO_ACTION)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reference_speech_id")
    private Speech referenceSpeech;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "referenceSpeech")
    private List<Speech> referencingSpeeches;

    @Setter
    private String fullAudioS3Url;

    @Column(nullable = false)
    @ColumnDefault("0")
    private Boolean recordDone;

    @Setter
    @Column(columnDefinition = "MEDIUMTEXT")
    private String userSymbol;

    @Setter
    @Column(nullable = false)
    @ColumnDefault("0")
    private Boolean bookmarked;

//    @Column(nullable = false)
//    @ColumnDefault("0")
//    private Boolean readyToChat;

    @Setter
    @OneToMany(mappedBy = "speech")
    private List<AudioSegment> audioSegments;

    @OneToMany(mappedBy = "speech")
    private List<AnalysisRecord> analysisRecords;

    @OneToMany(mappedBy = "speech")
    private List<AIChatLog> aiChatLogs;

    @Setter
    @Column(name = "relative_order", nullable = false)
    @ColumnDefault("1")
    private Integer order;

    @Column(nullable = true)
    private Integer feedbackCount;

    @Column(nullable = true)
    private Float avgLPM;

    @Column(nullable = true)
    private Float pauseRatio;

    @Column(nullable = true)
    private Float avgF0;

    public void setRecordDone() {
        this.recordDone = true;
    }

    public void disconnectReferenceSpeech() {
        this.referenceSpeech = null;
    }

    public void setPresignedAudioSegments(List<AudioSegment> audioSegments) {
        List<AudioSegment> copied = List.copyOf(audioSegments);
        this.audioSegments.clear();
        this.audioSegments.addAll(copied);
    }

//    public void setAIChatAvailable() {
//        this.readyToChat = true;
//    }
}
