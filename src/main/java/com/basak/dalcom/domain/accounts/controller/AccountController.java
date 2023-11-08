package com.basak.dalcom.domain.accounts.controller;

import com.basak.dalcom.domain.accounts.controller.dto.AccountRespDto;
import com.basak.dalcom.domain.accounts.controller.dto.CoachSignupReqDto;
import com.basak.dalcom.domain.accounts.controller.dto.LoginDto;
import com.basak.dalcom.domain.accounts.controller.dto.UserSignupReqDto;
import com.basak.dalcom.domain.accounts.data.Account;
import com.basak.dalcom.domain.accounts.service.AccountService;
import com.basak.dalcom.domain.accounts.service.dto.CoachSignupDto;
import com.basak.dalcom.domain.accounts.service.dto.UserSignupDto;
import com.basak.dalcom.spring.security.service.DalcomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = "accounts", description = "회원 관련 API")
@AllArgsConstructor
@RestController
@CrossOrigin(origins = {"http://localhost:3000",
    "http://localhost:8000"}, allowedHeaders = "*", allowCredentials = "true")
@RequestMapping("/api/v1/accounts")
public class AccountController {

    private final AccountService accountService;

    @Operation(
        summary = "일반 사용자 회원 가입 API",
        description = "일반 사용자에 대한 회원 가입을 수행하는 API"
    )
    @ApiResponse(responseCode = "201", description = "회원 가입 성공",
        content = @Content(schema = @Schema(implementation = AccountRespDto.class)))
    @ApiResponse(responseCode = "409", description = "이미 사용중인 username, email, phoneNumber인 경우", content = @Content)
    @PostMapping("/user/signup")
    public ResponseEntity<AccountRespDto> userSignup(@Validated @RequestBody UserSignupReqDto dto) {
        Account createdAccount = accountService.userSignUp(new UserSignupDto(dto));
        return new ResponseEntity<>(new AccountRespDto(createdAccount), HttpStatus.CREATED);
    }

    @Operation(
        summary = "코치 회원 가입 API"
    )
    @ApiResponse(responseCode = "201", description = "회원 가입 성공",
        content = @Content(schema = @Schema(implementation = AccountRespDto.class)))
    @ApiResponse(responseCode = "409", description = "이미 사용중인 username, email, phoneNumber인 경우", content = @Content)
    @PostMapping("/coach/signup")
    public ResponseEntity<AccountRespDto> coachSignup(
        @Validated @RequestBody CoachSignupReqDto dto) {
        Account createdAccount = accountService.coachSignUp(new CoachSignupDto(dto));
        return new ResponseEntity<>(new AccountRespDto(createdAccount), HttpStatus.CREATED);
    }

    @Operation(
        summary = "로그인 API"
    )
    @ApiResponse(responseCode = "200", description = "로그인 성공",
        content = @Content(schema = @Schema(implementation = AccountRespDto.class)))
    @ApiResponse(responseCode = "401", description = "로그인 실패", content = @Content)
    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LoginDto dto, HttpServletRequest request)
        throws Exception {
        // 세션 키가 없는 경우 빈 세션 생성
        HttpSession session = request.getSession(true);

        // 세션 키가 이미 있는 경우
        if (!session.isNew()) {
            // 무효화 후 세션 재할당
            session.invalidate();
            session = request.getSession(true);
        }

        String username = dto.getUsername();
        String password = dto.getPassword();

        // username, password로 사용자 검색
        Optional<Account> account = accountService.findByUsernameAndPassword(username,
            password);
        // 사용자가 검색된 경우
        if (account.isPresent()) {
            // 세션에 userId를 써줌
            session.setAttribute("userId", account.get().getId().toString());
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }


    @Operation(
        summary = "본인 정보 확인 API"
    )
    @Parameter(name = "userDetails", hidden = true)
    @ApiResponse(responseCode = "200", description = "조회 성공",
        content = @Content(schema = @Schema(implementation = AccountRespDto.class)))
    @GetMapping("/me")
    public ResponseEntity<AccountRespDto> me(
        @AuthenticationPrincipal DalcomUserDetails userDetails) {
        if (userDetails == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else {
            Integer id = Integer.parseInt(userDetails.getUsername());
            Account account = accountService.findById(id).get();
            return new ResponseEntity<>(new AccountRespDto(account), HttpStatus.OK);
        }
    }
}
