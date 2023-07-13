package com.basak.dalcom.domain.accounts.service;

import static com.basak.dalcom.domain.accounts.service.validators.AccountFieldValidator.uniqueValueDuplicationCheck;

import com.basak.dalcom.domain.accounts.controller.request_dto.UserSignupDto;
import com.basak.dalcom.domain.accounts.data.Account;
import com.basak.dalcom.domain.accounts.data.AccountRepository;
import com.basak.dalcom.domain.accounts.data.AccountRole;
import com.basak.dalcom.domain.accounts.service.exceptions.DuplicatedEmailException;
import com.basak.dalcom.domain.accounts.service.exceptions.DuplicatedPhoneNumberException;
import com.basak.dalcom.domain.common.service.exceptions.DuplicatedFieldException;
import com.basak.dalcom.domain.profiles.service.UserProfileService;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserProfileService userProfileService;

    /**
     * 일반 사용자에 대한 회원 가입을 수행하는 서비스로, Account 및 UserProfile이 생성 및 연결된다.
     *
     * @param requestDto 회원 가입에 필요한 정보가 담긴 dto
     * @return 생성된 Account entity (user profile이 연결되어 있음)
     * @throws DuplicatedEmailException:       중복된 이메일로 요청된 경우
     * @throws DuplicatedPhoneNumberException: 중복된 전화 번호로 요청된 경우
     */
    @Transactional
    public Account userSignUp(UserSignupDto requestDto)
        throws DuplicatedFieldException {
        uniqueValueDuplicationCheck(accountRepository,
            requestDto.getEmail(), requestDto.getPhoneNumber());

        // Account 생성
        Account account = Account.builder()
            .role(AccountRole.USER)
            .uuid(getUUID())
            .nickname(requestDto.getNickname())
            .email(requestDto.getEmail())
            .phoneNumber(requestDto.getPhoneNumber())
            .password(passwordEncoder.encode(requestDto.getPassword()))
            .build();
        accountRepository.save(account);

        // 생성된 account userProfile을 생성하여 연결
        userProfileService
            .createUserProfile(account, requestDto.getVoiceUsageAgreement());

        return account;
    }

    public Optional<Account> findUserAccountByUuid(String uuid) {
        Optional<Account> account = accountRepository.findByUuidAndRole(uuid, AccountRole.USER);
        return account;
    }

    private String getUUID() {
        return UUID.randomUUID().toString();
    }
}
