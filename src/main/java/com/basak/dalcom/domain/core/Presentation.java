package com.basak.dalcom.domain.core;

import com.basak.dalcom.domain.common.BaseDatetimeAudit;
import com.basak.dalcom.domain.profiles.UserProfile;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "presentation")
public class Presentation extends BaseDatetimeAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_profile_id", nullable = false)
    private UserProfile userProfile;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String outline;

    @Column(columnDefinition = "TEXT")
    private String checkpoint;

    @OneToMany(mappedBy = "presentation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Speech> speeches;
}
