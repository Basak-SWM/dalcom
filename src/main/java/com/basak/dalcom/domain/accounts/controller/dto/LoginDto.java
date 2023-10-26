package com.basak.dalcom.domain.accounts.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginDto {

    @Schema(description = "로그인 ID", type = "string", example = "qkrdbqls1001")
    private String username;
    @Schema(description = "로그인 비밀번호", type = "string", example = "asxczabjk!43")
    private String password;
}
