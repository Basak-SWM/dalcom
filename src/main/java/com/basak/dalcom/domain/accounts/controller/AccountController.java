package com.basak.dalcom.domain.accounts.controller;

import com.basak.dalcom.domain.accounts.controller.exceptions.DuplicatedAccountFieldException;
import com.basak.dalcom.domain.accounts.controller.request_dto.UserSignupDto;
import com.basak.dalcom.domain.accounts.controller.response_dto.SignupSuccessDto;
import com.basak.dalcom.domain.accounts.data.Account;
import com.basak.dalcom.domain.accounts.service.AccountService;
import com.basak.dalcom.domain.common.controllers.exceptions.FieldConflictResponseException;
import com.basak.dalcom.domain.common.service.exceptions.DuplicatedFieldException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "accounts", description = "회원 관련 API")
@AllArgsConstructor
@RestController
@Validated
@RequestMapping("/api/v1/accounts")
public class AccountController {

    private final AccountService accountService;

    @Operation(
        summary = "일반 사용자 회원 가입 API",
        description = "일반 사용자에 대한 회원 가입을 수행하는 API"
    )
    @ApiResponse(responseCode = "201", description = "회원 가입 성공",
        content = @Content(schema = @Schema(implementation = SignupSuccessDto.class)))
    @ApiResponse(responseCode = "400", description = "Validation 실패",
        content = @Content(schema = @Schema(implementation = Void.class)))
    @ApiResponse(responseCode = "409", description = "이미 사용중인 email이나 phoneNumber인 경우",
        content = @Content(schema = @Schema(implementation = Void.class)))
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

