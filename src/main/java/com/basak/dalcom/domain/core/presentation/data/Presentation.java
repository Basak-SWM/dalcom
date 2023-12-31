package com.basak.dalcom.domain.core.presentation.data;

import com.basak.dalcom.domain.common.BaseEntity;
import com.basak.dalcom.domain.core.speech.data.Speech;
import com.basak.dalcom.domain.profiles.data.UserProfile;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
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
import org.hibernate.annotations.ColumnDefault;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "presentation")
public class Presentation extends BaseEntity {

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

    @OneToMany(mappedBy = "presentation")
    private List<Speech> speeches;

    @Column(nullable = false)
    @ColumnDefault("0")
    private Integer speechAutoIncrementValue = 0;

    public void increaseSpeechAutoIncrementValue() {
        speechAutoIncrementValue++;
    }
}
