package com.basak.dalcom.domain.accounts;

import com.basak.dalcom.domain.accounts.dto.AccountSignupDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Integer signUp(AccountSignupDto requestDto) {
        return accountRepository.save(Account.builder()
                .nickname(requestDto.getNickname())
                .email(requestDto.getEmail())
                .phoneNumber(requestDto.getPhoneNumber())
                .password(passwordEncoder.encode(requestDto.getPassword()))
                .role(requestDto.getRole()).build()).getId();
    }
}
