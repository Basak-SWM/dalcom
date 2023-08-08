package com.basak.dalcom.domain.core.presentation.service;

import com.basak.dalcom.domain.accounts.data.Account;
import com.basak.dalcom.domain.accounts.service.AccountService;
import com.basak.dalcom.domain.common.exception.stereotypes.NotFoundException;
import com.basak.dalcom.domain.core.presentation.data.Presentation;
import com.basak.dalcom.domain.core.presentation.data.PresentationRepository;
import com.basak.dalcom.domain.core.presentation.service.dto.PresentationDto;
import com.basak.dalcom.domain.core.speech.data.Speech;
import com.basak.dalcom.domain.core.speech.service.SpeechService;
import com.basak.dalcom.domain.profiles.data.UserProfile;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Service
public class PresentationService {

    private final PresentationRepository presentationRepository;

    private final AccountService accountService;
    private final SpeechService speechService;

    public Presentation createPresentation(UUID accountUuid, PresentationDto dto) {
        Account account = accountService.findUserAccountByUuid(accountUuid)
            .orElseThrow(() -> new NotFoundException("Account"));

        UserProfile userProfile = account.getUserProfile();

        Presentation presentation = Presentation.builder()
            .userProfile(userProfile)
            .title(dto.getTitle())
            .outline(dto.getOutline())
            .checkpoint(dto.getCheckpoint())
            .speechAutoIncrementValue(0)
            .build();

        presentationRepository.save(presentation);

        return presentation;
    }

    public List<Presentation> getPresentationsByAccountUuid(UUID uuid) {
        Account account = accountService.findUserAccountByUuid(uuid)
            .orElseThrow(() -> new NotFoundException("Account"));
        UserProfile userProfile = account.getUserProfile();

        return presentationRepository.findPresentationsByUserProfile(userProfile);
    }

    @Transactional
    public void deleteById(Integer presentationId) {
        Presentation presentation = presentationRepository.findById(presentationId)
            .orElseThrow(() -> new NotFoundException("Presentation"));

        for (Speech speech : presentation.getSpeeches()) {
            speechService.deleteById(speech.getId());
        }

        presentationRepository.deleteById(presentationId);
    }
}
