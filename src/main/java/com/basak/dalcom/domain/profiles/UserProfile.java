package com.basak.dalcom.domain.profiles;

import com.basak.dalcom.domain.accounts.Account;
import com.basak.dalcom.domain.common.BaseDatetimeAudit;
import com.basak.dalcom.domain.core.Presentation;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "user_profile")
public class UserProfile extends BaseDatetimeAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @ColumnDefault("0")
    private boolean voiceUsageAgreement;

    @OneToMany(mappedBy = "userProfile")
    private List<Presentation> presentations;
}
