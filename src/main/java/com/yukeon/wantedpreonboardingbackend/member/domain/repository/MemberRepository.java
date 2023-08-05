package com.yukeon.wantedpreonboardingbackend.member.domain.repository;

import com.yukeon.wantedpreonboardingbackend.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

}
