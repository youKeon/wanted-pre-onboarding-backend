package com.yukeon.wantedpreonboardingbackend.member.util;

import com.yukeon.wantedpreonboardingbackend.member.domain.Member;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

public class MemberInfo extends User {
    private Member member;
    public MemberInfo(Member member) {
        super(member.getEmail(), member.getPassword(), List.of(new SimpleGrantedAuthority("ROLE_USER")));
        this.member = member;
    }
}
