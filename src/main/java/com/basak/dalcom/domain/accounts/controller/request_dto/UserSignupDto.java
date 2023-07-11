package com.basak.dalcom.domain.accounts.controller.request_dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class UserSignupDto {

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

    private Boolean voiceUsageAgreement;
}