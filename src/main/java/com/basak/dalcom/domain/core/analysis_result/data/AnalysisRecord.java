package com.basak.dalcom.domain.core.analysis_result.data;

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
import lombok.Setter;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "analysis_record")
@Entity
public class AnalysisRecord extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "speech_id", nullable = false)
    private Speech speech;

    @Column(nullable = false)
    @Convert(converter = AnalysisTypeConverter.class)
    private AnalysisType type;

    @Column(nullable = false, unique = true)
    private String url;

    @Transient
    private LocalDateTime lastModifiedDate;
}
