package com.basak.dalcom.domain.core.analysis_record.data;

import com.basak.dalcom.domain.common.BaseEntity;
import com.basak.dalcom.domain.core.speech.data.Speech;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Convert;
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

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "analysis_record")
public class AnalysisRecord extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Convert(converter = AnalysisRecordTypeConverter.class)
    private AnalysisRecordType type;

    @ManyToOne
    @JoinColumn(name = "speech_id", nullable = false)
    private Speech speech;

    @Transient
    private LocalDateTime lastModifiedDate;
}
