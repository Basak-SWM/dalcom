package com.basak.dalcom.domain.profiles.service;

import com.basak.dalcom.domain.accounts.data.Account;
import com.basak.dalcom.domain.common.exception.stereotypes.BadRequestException;
import com.basak.dalcom.domain.profiles.data.CoachProfile;
import com.basak.dalcom.domain.profiles.data.CoachProfileRepository;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public void update(Integer accountId,
        String shortIntroduce, String speciality, String introduce, String youtubeUrl) {
        CoachProfile coachProfile = coachProfileRepository.findByAccountId(accountId)
            .orElseThrow(() -> new BadRequestException("코치 프로필이 존재하지 않습니다."));

        coachProfile.setShortIntroduce(shortIntroduce);
        coachProfile.setSpeciality(speciality);
        coachProfile.setIntroduce(introduce);
        coachProfile.setYoutubeUrl(youtubeUrl);
    }

    @Transactional(readOnly = true)
    public List<CoachProfile> findAll() {
        return coachProfileRepository.findAll();
    }
}
