package com.yukeon.wantedpreonboardingbackend.member.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@AllArgsConstructor
@Getter
public class MemberSignInResponse {
    private String accessToken;

    public static MemberSignInResponse from(String accessToken) {
        return new MemberSignInResponse(accessToken);
    }
}
