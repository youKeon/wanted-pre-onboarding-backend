package com.yukeon.wantedpreonboardingbackend.member.persistence;

import com.yukeon.wantedpreonboardingbackend.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);
}
