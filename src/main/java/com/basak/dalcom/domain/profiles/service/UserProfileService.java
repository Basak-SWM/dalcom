package com.basak.dalcom.domain.profiles.service;

import com.basak.dalcom.domain.accounts.data.Account;
import com.basak.dalcom.domain.profiles.data.UserProfile;
import com.basak.dalcom.domain.profiles.data.UserProfileRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class UserProfileService {

    private final UserProfileRepository userProfileRepository;

    /**
     * 생성되어 있는 account와 연결할 UserProfile을 생성하는 서비스
     *
     * @param account             UserProfile을 연결할 account entity
     * @param voiceUsageAgreement 음성 데이터 사용 동의 여부
     * @return 생성된 UserProfile entity
     */
    public UserProfile createUserProfile(Account account, boolean voiceUsageAgreement) {
        UserProfile userProfile = UserProfile.builder()
            .account(account)
            .voiceUsageAgreement(voiceUsageAgreement)
            .build();
        userProfileRepository.save(userProfile);

        return userProfile;
    }
}
