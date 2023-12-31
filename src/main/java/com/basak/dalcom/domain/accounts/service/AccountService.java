package com.basak.dalcom.domain.accounts.service;

import static com.basak.dalcom.domain.accounts.service.validators.AccountFieldValidator.uniqueValueDuplicationCheck;

import com.basak.dalcom.domain.accounts.data.Account;
import com.basak.dalcom.domain.accounts.data.AccountRepository;
import com.basak.dalcom.domain.accounts.data.AccountRole;
import com.basak.dalcom.domain.accounts.service.dto.CoachSignupDto;
import com.basak.dalcom.domain.accounts.service.dto.UserSignupDto;
import com.basak.dalcom.domain.profiles.service.CoachProfileService;
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
    private final CoachProfileService coachProfileService;

    /**
     * 일반 사용자에 대한 회원 가입을 수행하는 서비스로, Account 및 UserProfile이 생성 및 연결된다.
     *
     * @param requestDto 회원 가입에 필요한 정보가 담긴 dto
     * @return 생성된 Account entity (user profile이 연결되어 있음)
     */
    @Transactional
    public Account userSignUp(UserSignupDto requestDto) {
        uniqueValueDuplicationCheck(accountRepository,
            requestDto.getUsername(), requestDto.getEmail(), requestDto.getPhoneNumber());

        // Account 생성
        Account account = Account.builder()
            .role(AccountRole.USER)
            .uuid(getUUID())
            .username(requestDto.getUsername())
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

    @Transactional
    public Account coachSignUp(CoachSignupDto dto) {
        uniqueValueDuplicationCheck(accountRepository,
            dto.getUsername(), dto.getEmail(), dto.getPhoneNumber());

        // Account 생성
        Account account = Account.builder()
            .role(AccountRole.COACH)
            .uuid(getUUID())
            .username(dto.getUsername())
            .nickname(dto.getNickname())
            .email(dto.getEmail())
            .phoneNumber(dto.getPhoneNumber())
            .password(passwordEncoder.encode(dto.getPassword()))
            .build();
        accountRepository.save(account);

        // 생성된 account userProfile을 생성하여 연결
        coachProfileService
            .createCoachProfile(account,
                dto.getShortIntroduce(), dto.getSpeciality(), dto.getIntroduce(),
                dto.getYoutubeUrl());

        return account;
    }


    @Transactional(readOnly = true)
    public Optional<Account> findByUsernameAndPassword(String username, String password) {
        Optional<Account> account = accountRepository.findByUsername(username);
        if (account.isPresent()) {
            if (passwordEncoder.matches(password, account.get().getPassword())) {
                return account;
            }
        }
        return Optional.empty();
    }

    @Transactional(readOnly = true)
    public Optional<Account> findById(Integer id) {
        Optional<Account> account = accountRepository.findById(id);
        return account;
    }

    public Optional<Account> findUserAccountByUuid(UUID uuid) {
        String stringUuid = uuid.toString();
        Optional<Account> account = accountRepository
            .findByUuidAndRole(stringUuid, AccountRole.USER);
        return account;
    }

    private String getUUID() {
        return UUID.randomUUID().toString();
    }
}
