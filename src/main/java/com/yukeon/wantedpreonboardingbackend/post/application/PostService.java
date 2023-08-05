package com.yukeon.wantedpreonboardingbackend.post.application;

import com.yukeon.wantedpreonboardingbackend.auth.exception.NoSuchMemberException;
import com.yukeon.wantedpreonboardingbackend.member.domain.Member;
import com.yukeon.wantedpreonboardingbackend.member.domain.repository.MemberRepository;
import com.yukeon.wantedpreonboardingbackend.member.util.MemberInfo;
import com.yukeon.wantedpreonboardingbackend.post.domain.repository.PostRepository;
import com.yukeon.wantedpreonboardingbackend.post.dto.request.PostCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    public void create(MemberInfo memberInfo, PostCreateRequest request) {
        Member member = memberRepository.findByEmail(memberInfo.getUsername()).orElseThrow(
                () -> new NoSuchMemberException()
        );
        postRepository.save(request.toEntity(member));
    }
}
