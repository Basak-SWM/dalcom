package com.basak.dalcom.domain.core;

import com.basak.dalcom.domain.common.BaseEntity;

import javax.persistence.*;
import java.util.List;

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

    @Column(columnDefinition = "MEDIUMTEXT")
    private String sttScript;

    @Column(columnDefinition = "MEDIUMTEXT")
    private String userSymbol;

    @OneToMany(mappedBy = "speech", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AudioSegment> audioSegments;
}
