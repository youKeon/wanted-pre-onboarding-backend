package com.yukeon.wantedpreonboardingbackend.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class TokenInfoResponse {
    private String grantType;
    private String accessToken;
    private String refreshToken;
}