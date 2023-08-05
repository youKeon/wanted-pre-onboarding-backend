package com.yukeon.wantedpreonboardingbackend.member.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@Getter
public class MemberSignInRequest {
    @Email
    @NotBlank(message = "공백일 수 없습니다.")
    private String email;
    @NotBlank(message = "공백일 수 없습니다.")
    private String password;
}
