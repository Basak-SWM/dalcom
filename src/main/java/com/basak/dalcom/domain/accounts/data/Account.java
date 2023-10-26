package com.basak.dalcom.domain.accounts.data;

import com.basak.dalcom.domain.common.BaseEntity;
import com.basak.dalcom.domain.profiles.data.CoachProfile;
import com.basak.dalcom.domain.profiles.data.UserProfile;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "account")
public class Account extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "CHAR(36)", unique = true, nullable = false)
    private String uuid;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    @Convert(converter = AccountRoleConverter.class)
    private AccountRole role;

    @OneToOne(mappedBy = "account")
    private UserProfile userProfile;

    @OneToOne(mappedBy = "account")
    private CoachProfile coachProfile;

    @Column(nullable = false, length = 20)
    private String nickname;

    @Column(unique = true, length = 450, nullable = false)
    private String email;

    @Column(unique = true, length = 24, nullable = false)
    private String phoneNumber;

    @Column(columnDefinition = "CHAR(64)")
    private String password;

    public void setUserProfile(UserProfile userProfile) {
        if (this.userProfile != null) {
            throw new IllegalStateException("이미 userProfile이 연결되어 있습니다.");
        } else if (this.role == AccountRole.COACH) {
            throw new IllegalStateException("coach는 userProfile을 가질 수 없습니다.");
        }

        this.userProfile = userProfile;
    }

    public void setCoachProfile(CoachProfile coachProfile) {
        if (this.coachProfile != null) {
            throw new IllegalStateException("이미 coachProfile이 연결되어 있습니다.");
        } else if (this.role == AccountRole.USER) {
            throw new IllegalStateException("user는 coachProfile을 가질 수 없습니다.");
        }

        this.coachProfile = coachProfile;
    }
}
