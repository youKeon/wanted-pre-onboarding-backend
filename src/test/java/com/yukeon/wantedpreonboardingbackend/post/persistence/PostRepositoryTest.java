package com.yukeon.wantedpreonboardingbackend.post.persistence;

import com.yukeon.wantedpreonboardingbackend.common.annotation.RepositoryTest;
import com.yukeon.wantedpreonboardingbackend.member.domain.Member;
import com.yukeon.wantedpreonboardingbackend.post.domain.Post;
import org.hibernate.Hibernate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


public class PostRepositoryTest extends RepositoryTest {
    private Post post;
    private Member member;

    @BeforeEach
    void setup() {
        member = memberRepository.save(new Member("yukeon97@gmail.com", "123"));
        post = postRepository.save(new Post(member, "content", "title"));
    }

    @Test
    @DisplayName("게시글 id를 입력 받아 게시글과 사용자를 함께 조회한다")
    public void findPostWithMember() throws Exception {
        // given
        Optional<Post> actual = postRepository.findPostWithMember(post.getId());

        // when, then
        assertThat(actual.get()).isNotNull();
        assertThat(actual.get().getId()).isEqualTo(post.getId());
        assertThat(actual.get().getMember().getEmail()).isEqualTo(member.getEmail());

        // 함께 조회된 Member가 Proxy가 아님
        assertThat(Hibernate.isInitialized(actual.get().getMember())).isTrue();
    }
}
