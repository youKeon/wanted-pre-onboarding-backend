package com.yukeon.wantedpreonboardingbackend.member.dto.response;

import com.yukeon.wantedpreonboardingbackend.member.domain.Member;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

public class MemberInfoResponse extends User {
    private Member member;
    public MemberInfoResponse(Member member) {
        super(member.getEmail(), member.getPassword(), List.of(new SimpleGrantedAuthority("ROLE_USER")));
        this.member = member;
    }
}
