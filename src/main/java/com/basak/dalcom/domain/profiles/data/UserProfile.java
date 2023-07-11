package com.basak.dalcom.domain.profiles.data;

import com.basak.dalcom.domain.accounts.data.Account;
import com.basak.dalcom.domain.common.BaseEntity;
import com.basak.dalcom.domain.core.presentation.data.Presentation;
import java.util.List;
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
import org.hibernate.annotations.ColumnDefault;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Table(name = "user_profile")
public class UserProfile extends BaseEntity {

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
