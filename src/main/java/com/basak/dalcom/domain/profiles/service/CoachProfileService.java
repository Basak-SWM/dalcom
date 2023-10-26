package com.basak.dalcom.domain.profiles.service;

import com.basak.dalcom.domain.accounts.data.Account;
import com.basak.dalcom.domain.profiles.data.CoachProfile;
import com.basak.dalcom.domain.profiles.data.CoachProfileRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class CoachProfileService {

    private final CoachProfileRepository coachProfileRepository;

    public CoachProfile createCoachProfile(Account account,
        String shortIntroduce, String speciality, String introduce, String youtubeUrl) {
        CoachProfile coachProfile = CoachProfile.builder()
            .account(account)
            .shortIntroduce(shortIntroduce)
            .speciality(speciality)
            .introduce(introduce)
            .youtubeUrl(youtubeUrl)
            .build();
        coachProfileRepository.save(coachProfile);

        account.setCoachProfile(coachProfile);

        return coachProfile;
    }
}
