package com.basak.dalcom.domain.accounts.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UserSignupDto {

    @Schema(description = "닉네임", type = "string", example = "박유빈")
    @NotBlank(message = "닉네임은 필수 입력 값입니다.")
    private String nickname;

    @Schema(description = "이메일", type = "string", example = "tokpeanutUser@gmail.com")
    @NotEmpty(message = "이메일은 필수 입력 값입니다.")
    @Email(message = "이메일 형식으로 입력해주세요.")
    private String email;

    @Schema(description = "전화번호", type = "string", example = "010-1234-5678")
    @NotEmpty(message = "휴대전화 번호는 필수 입력 값입니다.")
    @Length(min = 10, max = 24, message = "휴대전화 번호는 10자 이상, 24자 이하로 입력해주세요.")
    private String phoneNumber;

    @Schema(description = "비밀번호", type = "string", example = "mysecretpw777!")
    @NotEmpty(message = "비밀번호는 필수 입력 값입니다.")
    @Length(min = 4, max = 16, message = "비밀번호는 4자 이상, 16자 이하로 입력해주세요.")
    private String password;

    @Schema(description = "음성 데이터 활용 동의 여부", type = "boolean", example = "true")
    private Boolean voiceUsageAgreement;

    public com.basak.dalcom.domain.accounts.service.dto.UserSignupDto toServiceDto() {
        return com.basak.dalcom.domain.accounts.service.dto.UserSignupDto.builder()
            .nickname(nickname)
            .email(email)
            .phoneNumber(phoneNumber)
            .password(password)
            .voiceUsageAgreement(voiceUsageAgreement)
            .build();
    }
}