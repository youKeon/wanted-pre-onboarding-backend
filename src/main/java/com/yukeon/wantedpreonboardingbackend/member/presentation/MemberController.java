package com.yukeon.wantedpreonboardingbackend.member.presentation;

import com.yukeon.wantedpreonboardingbackend.member.application.MemberService;
import com.yukeon.wantedpreonboardingbackend.member.dto.request.MemberSignInRequest;
import com.yukeon.wantedpreonboardingbackend.member.dto.request.MemberSignUpRequest;
import com.yukeon.wantedpreonboardingbackend.member.dto.response.MemberSignInResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/signup")
    public void signUp(@RequestBody MemberSignUpRequest request) {
        memberService.signUp(request);
    }

    @GetMapping("/signin")
    public MemberSignInResponse signIn(@RequestBody MemberSignInRequest request) {
        return memberService.signIn(request);
    }
}
