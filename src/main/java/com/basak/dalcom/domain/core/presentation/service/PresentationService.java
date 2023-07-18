package com.basak.dalcom.domain.core.presentation.service;

import com.basak.dalcom.domain.accounts.data.Account;
import com.basak.dalcom.domain.accounts.service.AccountService;
import com.basak.dalcom.domain.accounts.service.exceptions.AccountNotFoundException;
import com.basak.dalcom.domain.core.presentation.controller.dto.PresentationCreateDto;
import com.basak.dalcom.domain.core.presentation.data.Presentation;
import com.basak.dalcom.domain.core.presentation.data.PresentationRepository;
import com.basak.dalcom.domain.profiles.data.UserProfile;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class PresentationService {

    private final AccountService accountService;
    private final PresentationRepository presentationRepository;

    public Presentation createPresentation(PresentationCreateDto dto)
        throws AccountNotFoundException {
        Account account = accountService.findUserAccountByUuid(dto.getAccountUuid())
            .orElseThrow(() -> new AccountNotFoundException("uuid=" + dto.getAccountUuid()));

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

    public Slice<Presentation> getPresentationsOf(String uuid, Pageable pageable)
        throws AccountNotFoundException {
        Account account = accountService.findUserAccountByUuid(uuid)
            .orElseThrow(() -> new AccountNotFoundException("uuid=" + uuid));
        UserProfile userProfile = account.getUserProfile();

        return presentationRepository.findSliceByUserProfile(userProfile, pageable);
    }
}
