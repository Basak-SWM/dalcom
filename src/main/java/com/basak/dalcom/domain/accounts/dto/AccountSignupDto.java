package com.basak.dalcom.domain.accounts.dto;

import com.basak.dalcom.domain.accounts.Account;
import com.basak.dalcom.domain.accounts.AccountRole;
import com.basak.dalcom.domain.common.ValueOfEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class AccountSignupDto {

    @NotBlank(message = "닉네임은 필수 입력 값입니다.")
    private String nickname;

    @NotEmpty(message = "이메일은 필수 입력 값입니다.")
    @Email(message = "이메일 형식으로 입력해주세요.")
    private String email;

    @NotEmpty(message = "휴대전화 번호는 필수 입력 값입니다.")
    @Length(min = 10, max = 24, message = "휴대전화 번호는 10자 이상, 24자 이하로 입력해주세요.")
    private String phoneNumber;

    @NotEmpty(message = "비밀번호는 필수 입력 값입니다.")
    @Length(min = 4, max = 16, message = "비밀번호는 4자 이상, 16자 이하로 입력해주세요.")
    private String password;

    // Enum Validation을 위한 Custom Annotation 지정.
    @ValueOfEnum(enumClass = AccountRole.class, message = "역할은 USER, COACH 중 하나여야 합니다.")
    private AccountRole role;

    public Account toEntity() {
        return Account.builder()
                .nickname(nickname)
                .email(email)
                .phoneNumber(phoneNumber)
                .password(password)
                .role(role)
                .build();
    }
}