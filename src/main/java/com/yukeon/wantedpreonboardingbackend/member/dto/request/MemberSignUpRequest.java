package com.yukeon.wantedpreonboardingbackend.member.dto.request;

import com.yukeon.wantedpreonboardingbackend.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@AllArgsConstructor
@Getter
public class MemberSignUpRequest {
    @Email
    @NotBlank(message = "공백일 수 없습니다.")
    private String email;
    @Size(min = 8)
    @NotBlank(message = "공백일 수 없습니다.")
    private String password;

    public Member toEntity(String encodedPassword) {
        return new Member(email, encodedPassword);
    }

}
