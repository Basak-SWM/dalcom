package com.basak.dalcom.domain.coaching_request.data;

import com.basak.dalcom.domain.coaching_request.CoachingRequest.Status;
import com.basak.dalcom.domain.common.BaseEntity;
import com.basak.dalcom.domain.profiles.data.CoachProfile;
import com.basak.dalcom.domain.profiles.data.UserProfile;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Table(name = "coaching_request")
public class CoachingRequest extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Status status;

    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne
    @JoinColumn(name = "user_profile_id", nullable = false)
    private UserProfile userProfile;

    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne
    @JoinColumn(name = "coach_profile_id", nullable = false)
    private CoachProfile coachProfile;

    private String userMessage;

    private String title;
    private String outline;
    private String checkpoint;

    private String fullAudioUrl;
    private String sttResult;

    private String coachMessage;
    private String jsonUserSymbol;
}
