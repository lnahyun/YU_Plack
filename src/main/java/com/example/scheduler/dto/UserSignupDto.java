package com.example.scheduler.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserSignupDto {

    @NotBlank(message = "이름을 입력해주세요.")
    private String username;

    @Size(min = 6, max = 15, message = "비밀번호는 6자 이상, 15자 이하로 입력해야 합니다.")
    private String password;

    @Email(message = "올바른 이메일 형식이어야 합니다.")
    @NotBlank(message = "이메일을 입력해주세요.")
    private String email;
}
