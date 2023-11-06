package com.basak.dalcom.domain.profiles.data;

import com.basak.dalcom.domain.accounts.data.Account;
import com.basak.dalcom.domain.coaching_request.data.CoachingRequest;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Table(name = "coach_profile")
public class CoachProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "coach_account_id")
    private Account account;

    @Column(columnDefinition = "CHAR(128)")
    private String shortIntroduce;

    @Column(columnDefinition = "CHAR(64)")
    private String speciality;

    @Column(columnDefinition = "TEXT")
    private String introduce;

    @Column(columnDefinition = "TEXT")
    private String youtubeUrl;

    @OneToMany(mappedBy = "coachProfile")
    private List<CoachingRequest> coachingRequests;
}
