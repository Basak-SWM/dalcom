package com.basak.dalcom.domain.accounts;

import com.basak.dalcom.domain.common.BaseDatetimeAudit;
import com.basak.dalcom.domain.profiles.UserProfile;

import javax.persistence.*;


@Entity
@Table(name = "account")
public class Account extends BaseDatetimeAudit {
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
}
