package com.yukeon.wantedpreonboardingbackend.auth.application;

import com.yukeon.wantedpreonboardingbackend.member.exception.NoSuchMemberException;
import com.yukeon.wantedpreonboardingbackend.member.domain.Member;
import com.yukeon.wantedpreonboardingbackend.member.persistence.MemberRepository;
import com.yukeon.wantedpreonboardingbackend.member.util.MemberInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return memberRepository.findByEmail(email)
                .map(this::createUserDetails)
                .orElseThrow(() -> new NoSuchMemberException());
    }

    private UserDetails createUserDetails(Member member) {
        return new MemberInfo(member);
    }
}