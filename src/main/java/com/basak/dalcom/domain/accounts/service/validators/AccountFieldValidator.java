package com.basak.dalcom.domain.accounts.service.validators;

import com.basak.dalcom.domain.accounts.data.AccountRepository;
import com.basak.dalcom.domain.common.exception.HandledException;
import org.springframework.http.HttpStatus;

/**
 * Account를 구성하는 일부 필드에 대한 validation을 수행하는 메서드의 모음
 */
public class AccountFieldValidator {

    private AccountFieldValidator() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 중복이 허용되지 않은 account의 각 필드들에 대한 중복 여부 검사를 수행하는 메서드
     *
     * @param repository  탐색할 repository
     * @param email       확인할 이메일
     * @param phoneNumber 확인할 전화 번호
     */
    public static void uniqueValueDuplicationCheck(AccountRepository repository,
        String username, String email, String phoneNumber) {
        usernameDuplicationCheck(repository, username);
        emailDuplicationCheck(repository, email);
        phoneNumberDuplicationCheck(repository, phoneNumber);
    }

    /**
     * 이메일 중복 검사를 수행하는 메서드
     *
     * @param repository 탐색할 repository
     * @param email      확인할 이메일
     */
    private static void emailDuplicationCheck(AccountRepository repository, String email) {
        repository.findByEmail(email)
            .ifPresent(account -> {
                throw new HandledException(HttpStatus.CONFLICT, "Duplicated email");
            });
    }

    /**
     * 전화 번호 중복 검사를 수행하는 메서드
     *
     * @param repository  탐색할 repository
     * @param phoneNumber 확인할 전화 번호
     */
    private static void phoneNumberDuplicationCheck(AccountRepository repository,
        String phoneNumber) {
        repository.findByPhoneNumber(phoneNumber)
            .ifPresent(account -> {
                throw new HandledException(HttpStatus.CONFLICT, "Duplicated phoneNumber");
            });
    }

    private static void usernameDuplicationCheck(AccountRepository repository, String username) {
        repository.findByUsername(username)
            .ifPresent(account -> {
                throw new HandledException(HttpStatus.CONFLICT, "Duplicated username");
            });
    }

}
