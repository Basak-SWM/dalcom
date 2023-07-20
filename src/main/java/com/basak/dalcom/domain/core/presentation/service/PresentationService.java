package com.basak.dalcom.domain.core.presentation.service;

import com.basak.dalcom.domain.accounts.data.Account;
import com.basak.dalcom.domain.accounts.service.AccountService;
import com.basak.dalcom.domain.common.exception.stereotypes.NotFoundException;
import com.basak.dalcom.domain.core.presentation.controller.dto.PresentationDto;
import com.basak.dalcom.domain.core.presentation.data.Presentation;
import com.basak.dalcom.domain.core.presentation.data.PresentationRepository;
import com.basak.dalcom.domain.profiles.data.UserProfile;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class PresentationService {

    private final AccountService accountService;
    private final PresentationRepository presentationRepository;

    public Presentation createPresentation(UUID accountUuid, PresentationDto dto) {
        Account account = accountService.findUserAccountByUuid(accountUuid)
            .orElseThrow(() -> new NotFoundException("Account"));

        UserProfile userProfile = account.getUserProfile();

        Presentation presentation = Presentation.builder()
            .userProfile(userProfile)
            .title(dto.getTitle())
            .outline(dto.getOutline())
            .checkpoint(dto.getCheckpoint())
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
}
