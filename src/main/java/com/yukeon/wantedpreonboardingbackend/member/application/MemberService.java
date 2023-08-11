package com.yukeon.wantedpreonboardingbackend.member.application;

import com.yukeon.wantedpreonboardingbackend.auth.jwt.JwtTokenProvider;
import com.yukeon.wantedpreonboardingbackend.member.domain.Member;
import com.yukeon.wantedpreonboardingbackend.member.exception.NoSuchMemberException;
import com.yukeon.wantedpreonboardingbackend.member.persistence.MemberRepository;
import com.yukeon.wantedpreonboardingbackend.member.dto.request.MemberSignInRequest;
import com.yukeon.wantedpreonboardingbackend.member.dto.request.MemberSignUpRequest;
import com.yukeon.wantedpreonboardingbackend.member.dto.response.MemberSignInResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    public void signUp(MemberSignUpRequest request) {
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        Member member = request.toEntity(encodedPassword);
        memberRepository.save(member);
    }

    public MemberSignInResponse signIn(MemberSignInRequest request) {
        Member member = memberRepository.findByEmail(request.getEmail()).orElseThrow(
                () -> new NoSuchMemberException("로그인에 실패했습니다.")
        );
        if (!passwordEncoder.matches(request.getPassword(), member.getPassword()))
            throw new NoSuchMemberException("로그인에 실패했습니다.");

        String accessToken = jwtTokenProvider.generateToken(request.getEmail());
        return MemberSignInResponse.from(accessToken);
    }
}
