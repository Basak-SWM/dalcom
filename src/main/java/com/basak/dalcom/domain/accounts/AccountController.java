package com.basak.dalcom.domain.accounts;

import com.basak.dalcom.domain.accounts.dto.AccountSignupDto;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Validated
@RequestMapping("/accounts")
public class AccountController {
    @PostMapping
    public int AccountSignup(@Valid @RequestBody AccountSignupDto request) {
        return 0;
    }
}

