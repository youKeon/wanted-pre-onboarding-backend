package com.yukeon.wantedpreonboardingbackend.member.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public class MemberSignInResponse {
    private String accessToken;

    public static MemberSignInResponse from(String accessToken) {
        return new MemberSignInResponse(accessToken);
    }
}
