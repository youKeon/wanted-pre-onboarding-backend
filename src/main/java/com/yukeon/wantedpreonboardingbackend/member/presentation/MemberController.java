package com.yukeon.wantedpreonboardingbackend.member.presentation;

import com.yukeon.wantedpreonboardingbackend.member.application.MemberService;
import com.yukeon.wantedpreonboardingbackend.member.dto.request.MemberSignInRequest;
import com.yukeon.wantedpreonboardingbackend.member.dto.request.MemberSignUpRequest;
import com.yukeon.wantedpreonboardingbackend.member.dto.response.MemberSignInResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/signup")
    public void signUp(@RequestBody @Valid MemberSignUpRequest request) {
        memberService.signUp(request);
    }

    @PostMapping("/signin")
    public MemberSignInResponse signIn(@RequestBody @Valid MemberSignInRequest request) {
        return memberService.signIn(request);
    }
}
