package com.yukeon.wantedpreonboardingbackend.post.application;

import com.yukeon.wantedpreonboardingbackend.member.exception.NoSuchMemberException;
import com.yukeon.wantedpreonboardingbackend.member.domain.Member;
import com.yukeon.wantedpreonboardingbackend.member.exception.UnAuthorizedMemberException;
import com.yukeon.wantedpreonboardingbackend.member.persistence.MemberRepository;
import com.yukeon.wantedpreonboardingbackend.member.util.MemberInfo;
import com.yukeon.wantedpreonboardingbackend.post.domain.Post;
import com.yukeon.wantedpreonboardingbackend.post.persistence.PostRepository;
import com.yukeon.wantedpreonboardingbackend.post.dto.request.PostCreateRequest;
import com.yukeon.wantedpreonboardingbackend.post.dto.request.PostUpdateRequest;
import com.yukeon.wantedpreonboardingbackend.post.dto.response.PostResponse;
import com.yukeon.wantedpreonboardingbackend.post.dto.response.PostsResponse;
import com.yukeon.wantedpreonboardingbackend.post.exception.NoSuchPostException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
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

        if (allPost.getNumberOfElements() == 0) throw new NoSuchPostException();

        return PostsResponse.of(allPost);
    }

    public PostResponse find(Long id) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new NoSuchPostException()
        );
        return PostResponse.from(post);
    }

    public void update(Long id, MemberInfo memberInfo, PostUpdateRequest request) {
        Post post = postRepository.findPostWithMember(id).orElseThrow(
                () -> new NoSuchPostException()
        );

        Member member = memberRepository.findByEmail(memberInfo.getUsername()).orElseThrow(
                () -> new NoSuchMemberException()
        );

        if (member.getId() != post.getMember().getId())
            throw new UnAuthorizedMemberException("수정 권한이 없는 사용자입니다.", HttpStatus.FORBIDDEN);

        post.update(request.getTitle(), request.getContent());
    }

    public void delete(Long id, MemberInfo memberInfo) {
        Post post = postRepository.findPostWithMember(id).orElseThrow(
                () -> new NoSuchPostException()
        );

        Member member = memberRepository.findByEmail(memberInfo.getUsername()).orElseThrow(
                () -> new NoSuchMemberException()
        );

        if (member.getId() != post.getMember().getId())
            throw new UnAuthorizedMemberException("삭제 권한이 없는 사용자입니다.", HttpStatus.FORBIDDEN);

        postRepository.delete(post);
    }
}
