package com.yukeon.wantedpreonboardingbackend.member.dto.request;

import com.yukeon.wantedpreonboardingbackend.member.domain.Member;
import com.yukeon.wantedpreonboardingbackend.member.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@AllArgsConstructor
@Getter
public class MemberSignUpRequest {
    @Email(message = "유효하지 않은 이메일 양식입니다.")
    @NotBlank(message = "공백일 수 없습니다.")
    private String email;
    @Size(min = 8, message = "최소 8글자 이상 비밀번호를 입력해 주세요")
    @NotBlank(message = "공백일 수 없습니다.")
    private String password;

    public Member toEntity(String encodedPassword) {
        return new Member(email, encodedPassword, Role.ROLE_USER);
    }

}
