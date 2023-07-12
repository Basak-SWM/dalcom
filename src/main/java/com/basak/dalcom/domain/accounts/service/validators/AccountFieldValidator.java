package com.basak.dalcom.domain.accounts.service.validators;

import com.basak.dalcom.domain.accounts.data.AccountRepository;
import com.basak.dalcom.domain.accounts.service.exceptions.DuplicatedEmailException;
import com.basak.dalcom.domain.accounts.service.exceptions.DuplicatedPhoneNumberException;
import com.basak.dalcom.domain.common.service.exceptions.DuplicatedFieldException;

/**
 * Account를 구성하는 일부 필드에 대한 validation을 수행하는 메서드의 모음
 */
@Deprecated
public final class AccountFieldValidator {

    private AccountFieldValidator() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 중복이 허용되지 않은 account의 각 필드들에 대한 중복 여부 검사를 수행하는 메서드
     *
     * @param repository  탐색할 repository
     * @param email       확인할 이메일
     * @param phoneNumber 확인할 전화 번호
     * @throws DuplicatedFieldException 이미 사용중인 값이 전달된 경우
     */
    public static void uniqueValueDuplicationCheck(AccountRepository repository,
        String email, String phoneNumber)
        throws DuplicatedFieldException {
        emailDuplicationCheck(repository, email);
        phoneNumberDuplicationCheck(repository, phoneNumber);
    }

    /**
     * 이메일 중복 검사를 수행하는 메서드
     *
     * @param repository 탐색할 repository
     * @param email      확인할 이메일
     * @throws DuplicatedEmailException 이미 사용중인 이메일인 경우
     */
    private static void emailDuplicationCheck(AccountRepository repository, String email)
        throws DuplicatedEmailException {
        repository.findByEmail(email)
            .ifPresent(account -> {
                throw new DuplicatedEmailException();
            });
    }

    /**
     * 전화 번호 중복 검사를 수행하는 메서드
     *
     * @param repository  탐색할 repository
     * @param phoneNumber 확인할 전화 번호
     * @throws DuplicatedPhoneNumberException 이미 사용중인 전화 번호인 경우
     */
    private static void phoneNumberDuplicationCheck(AccountRepository repository,
        String phoneNumber) throws DuplicatedPhoneNumberException {
        repository.findByPhoneNumber(phoneNumber)
            .ifPresent(account -> {
                throw new DuplicatedPhoneNumberException();
            });
    }
}
