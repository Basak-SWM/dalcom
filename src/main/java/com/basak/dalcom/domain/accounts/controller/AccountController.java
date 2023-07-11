package com.basak.dalcom.domain.accounts.controller;

import com.basak.dalcom.domain.accounts.controller.exceptions.DuplicatedAccountFieldException;
import com.basak.dalcom.domain.accounts.controller.request_dto.UserSignupDto;
import com.basak.dalcom.domain.accounts.controller.response_dto.SignupSuccessDto;
import com.basak.dalcom.domain.accounts.data.Account;
import com.basak.dalcom.domain.accounts.service.AccountService;
import com.basak.dalcom.domain.common.controllers.exceptions.FieldConflictResponseException;
import com.basak.dalcom.domain.common.service.exceptions.DuplicatedFieldException;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@Validated
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;

    /**
     * 일반 사용자에 대한 회원 가입을 수행하는 API
     *
     * @param dto 회원 가입에 필요한 정보를 담은 DTO
     * @return (201) 가입된 회원의 정보 (uuid)
     * @return (400) Validating 실패
     * @return (409) 이미 사용중인 email이나 phoneNumber로 요청된 경우
     */
    @PostMapping("/user/signup")
    public ResponseEntity<SignupSuccessDto> userSignup(@Valid @RequestBody UserSignupDto dto)
        throws FieldConflictResponseException {
        try {
            Account createdAccount = accountService.userSignUp(dto);
            return new ResponseEntity<>(new SignupSuccessDto(createdAccount), HttpStatus.CREATED);
        } catch (DuplicatedFieldException exception) {
            throw new DuplicatedAccountFieldException(exception.getFieldName());
        }
    }
}

