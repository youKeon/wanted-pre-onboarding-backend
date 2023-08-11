package com.yukeon.wantedpreonboardingbackend.post.domain;

import com.yukeon.wantedpreonboardingbackend.member.domain.Member;
import com.yukeon.wantedpreonboardingbackend.member.util.MemberInfo;
import com.yukeon.wantedpreonboardingbackend.post.exception.InvalidPostException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class PostTest {
    private Member member;
    private MemberInfo memberInfo;

    @BeforeEach
    void setup() {
        member = new Member("yukeon97@gmail.com", "123");
        memberInfo = new MemberInfo(member);
        ReflectionTestUtils.setField(member, "id", 1L);
    }

    @Test
    @DisplayName("게시글을 생성한다")
    public void createPost() throws Exception {
        //given, when, then
        assertDoesNotThrow(() -> new Post(member, "content", "title"));
    }

    @Test
    @DisplayName("게시글 생성 시 content가 공백이면 예외가 발생한다")
    public void createPostEmptyContentException() throws Exception {
        //given, when, then
        assertThatThrownBy(() -> new Post(member, "title", ""))
                .isInstanceOf(InvalidPostException.class)
                .hasMessageContaining("Content는 공백일 수 없습니다.");;
    }

    @Test
    @DisplayName("게시글 생성 시 title이 공백이면 예외가 발생한다")
    public void createPostEmptyTitleException() throws Exception {
        //given, when, then
        assertThatThrownBy(() -> new Post(member, "", "content"))
                .isInstanceOf(InvalidPostException.class)
                .hasMessageContaining("Title은 공백일 수 없습니다.");
    }

}
