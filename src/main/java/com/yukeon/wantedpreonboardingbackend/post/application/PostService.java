package com.yukeon.wantedpreonboardingbackend.post.application;

import com.yukeon.wantedpreonboardingbackend.member.exception.NoSuchMemberException;
import com.yukeon.wantedpreonboardingbackend.member.domain.Member;
import com.yukeon.wantedpreonboardingbackend.member.domain.repository.MemberRepository;
import com.yukeon.wantedpreonboardingbackend.member.util.MemberInfo;
import com.yukeon.wantedpreonboardingbackend.post.domain.Post;
import com.yukeon.wantedpreonboardingbackend.post.domain.repository.PostRepository;
import com.yukeon.wantedpreonboardingbackend.post.dto.request.PostCreateRequest;
import com.yukeon.wantedpreonboardingbackend.post.dto.response.PostResponse;
import com.yukeon.wantedpreonboardingbackend.post.dto.response.PostsResponse;
import com.yukeon.wantedpreonboardingbackend.post.exception.NoSuchPostException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public PostsResponse findAll(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Post> allPost = postRepository.findAll(pageRequest);
        return PostsResponse.of(allPost);
    }

    public PostResponse find(Long id) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new NoSuchPostException()
        );
        return PostResponse.from(post);
    }
}
