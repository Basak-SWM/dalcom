package com.basak.dalcom.domain.accounts.data;

import com.basak.dalcom.domain.common.BaseEntity;
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

    @Column(nullable = false)
    @Convert(converter = AccountRoleConverter.class)
    private AccountRole role;

    @OneToOne(mappedBy = "account")
    private UserProfile userProfile;

    @Column(nullable = false, length = 20)
    private String nickname;

    @Column(unique = true, length = 450, nullable = false)
    private String email;

    @Column(unique = true, length = 24, nullable = false)
    private String phoneNumber;

    @Column(columnDefinition = "CHAR(64)")
    private String password;

    public void connectUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }
}
