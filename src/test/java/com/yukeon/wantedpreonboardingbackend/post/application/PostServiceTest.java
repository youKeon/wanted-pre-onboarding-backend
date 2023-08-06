package com.yukeon.wantedpreonboardingbackend.post.application;

import com.yukeon.wantedpreonboardingbackend.member.domain.Member;
import com.yukeon.wantedpreonboardingbackend.member.exception.InvalidMemberException;
import com.yukeon.wantedpreonboardingbackend.member.exception.NoSuchMemberException;
import com.yukeon.wantedpreonboardingbackend.member.persistence.MemberRepository;
import com.yukeon.wantedpreonboardingbackend.member.util.MemberInfo;
import com.yukeon.wantedpreonboardingbackend.post.domain.Post;
import com.yukeon.wantedpreonboardingbackend.post.dto.request.PostCreateRequest;
import com.yukeon.wantedpreonboardingbackend.post.dto.request.PostUpdateRequest;
import com.yukeon.wantedpreonboardingbackend.post.dto.response.PostResponse;
import com.yukeon.wantedpreonboardingbackend.post.dto.response.PostsResponse;
import com.yukeon.wantedpreonboardingbackend.post.exception.NoSuchPostException;
import com.yukeon.wantedpreonboardingbackend.post.persistence.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@Transactional
public class PostServiceTest {
    @InjectMocks
    private PostService postService;
    @Mock
    private PostRepository postRepository;
    @Mock
    private MemberRepository memberRepository;
    private static Pageable pageable = PageRequest.of(0, 3);
    private Member member;
    private MemberInfo memberInfo;
    private Post post1;
    private Post post2;
    private Post post3;
    @BeforeEach
    void setup() {
        member = new Member("yukeon97@gmail.com", "123");
        memberInfo = new MemberInfo(member);

        post1 = new Post(member, "content1", "title1");
        post2 = new Post(member, "content2", "title2");
        post3 = new Post(member, "content3", "title3");

        ReflectionTestUtils.setField(member, "id", 1L);
        ReflectionTestUtils.setField(post1, "id", 1L);
        ReflectionTestUtils.setField(post2, "id", 2L);
        ReflectionTestUtils.setField(post3, "id", 3L);
    }

    @Test
    @DisplayName("게시글을 생성한다")
    public void createPost() throws Exception {
        // given
        PostCreateRequest request = new PostCreateRequest(post1.getTitle(), post1.getContent());

        // when
        when(memberRepository.findByEmail(memberInfo.getUsername())).thenReturn(Optional.of(member));
        when(postRepository.save(any(Post.class))).thenReturn(post1);
        postService.create(memberInfo, request);

        // then
        verify(memberRepository).findByEmail(memberInfo.getUsername());
        verify(postRepository).save(any(Post.class));
    }

    @Test
    @DisplayName("게시글 생성 시 존재하지 않는 사용자의 경우 예외가 발생한다")
    public void createPostNoSuchMemberException() throws Exception {
        // given
        PostCreateRequest request = new PostCreateRequest(post1.getTitle(), post1.getContent());

        // when
        when(memberRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        // then
        assertThatThrownBy(
                () -> postService.create(memberInfo, request))
                .isInstanceOf(NoSuchMemberException.class)
                .hasMessageContaining("존재하지 않는 사용자입니다.");
    }

    @Test
    @DisplayName("게시글 id로 게시글을 단건 조회한다")
    public void findPostByIdTest() throws Exception {
        // when
        when(postRepository.findById(post1.getId())).thenReturn(Optional.ofNullable(post1));
        PostResponse actual = postService.find(post1.getId());

        // then
        assertAll(
                () -> assertThat(actual.getContent()).isEqualTo(post1.getContent()),
                () -> assertThat(actual.getTitle()).isEqualTo(post1.getTitle())
        );
    }

    @Test
    @DisplayName("존재하지 않는 게시글 id로 단건 조회를 하는 경우 예외가 발생한다")
    public void findPostNotExistIdExceptionTest() throws Exception {
        // given
        Long 존재하지_않는_게시글의_id = 1L;

        // when, then
        assertThatThrownBy(
                () -> postService.find(존재하지_않는_게시글의_id))
                .isInstanceOf(NoSuchPostException.class)
                .hasMessageContaining("존재하지 않는 게시글입니다.");
    }

    @Test
    @DisplayName("주어진 페이지 개수만큼 게시글을 조회한다")
    public void findAllPostTest() throws Exception {
        // given
        Page<Post> postPage = new PageImpl<>(List.of(post1, post2, post3), pageable, 3);

        // when
        when(postRepository.findAll(pageable)).thenReturn(postPage);
        PostsResponse response = postService.findAll(0, 3);

        // then
        assertAll(
                () -> assertThat(response.getNumberOfPosts()).isEqualTo(3),
                () -> assertThat(response.getPostInfoResponses().size()).isEqualTo(3),
                () -> assertThat(response.getIsLastPage()).isTrue()
        );
    }

    @Test
    @DisplayName("게시글이 없으면 예외가 발생한다")
    public void findAllPostNoSuchPostExceptionTest() throws Exception {
        // given
        Page<Post> postPage = Page.empty();

        // when
        when(postRepository.findAll(pageable)).thenReturn(postPage);

        // then
        assertThrows(NoSuchPostException.class,
                () -> postService.findAll(0, 3));
    }

    @Test
    @DisplayName("게시글을 수정한다")
    public void updatePostTest() throws Exception {
        //given
        PostUpdateRequest request = new PostUpdateRequest("afterTitle", "afterContent");

        //when
        when(postRepository.findPostWithMember(post1.getId())).thenReturn(Optional.ofNullable(post1));
        when(memberRepository.findByEmail(member.getEmail())).thenReturn(Optional.ofNullable(member));
        postService.update(post1.getId(), memberInfo, request);

        //then
        assertAll(
                () -> assertThat(post1.getContent()).isEqualTo(request.getContent()),
                () -> assertThat(post1.getTitle()).isEqualTo(request.getTitle())
        );
    }

    @Test
    @DisplayName("존재하지 않는 게시글을 수정하면 예외가 발생한다")
    public void updatePostNoSuchPostExceptionTest() throws Exception {
        //given
        Long 존재하지_않는_게시글의_id = 1L;
        PostUpdateRequest request = new PostUpdateRequest("afterTitle", "afterContent");

        // when, then
        assertThatThrownBy(
                () -> postService.update(존재하지_않는_게시글의_id, memberInfo, request))
                .isInstanceOf(NoSuchPostException.class)
                .hasMessageContaining("존재하지 않는 게시글입니다.");
    }

    @Test
    @DisplayName("존재하지 않는 사용자의 게시글을 수정하면 예외가 발생한다")
    public void updatePostNoSuchMemberExceptionTest() throws Exception {
        //given
        PostUpdateRequest request = new PostUpdateRequest("afterTitle", "afterContent");

        // when, then
        when(postRepository.findPostWithMember(post1.getId())).thenReturn(Optional.ofNullable(post1));
        when(memberRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThatThrownBy(
                () -> postService.update(post1.getId(), memberInfo, request))
                .isInstanceOf(NoSuchMemberException.class)
                .hasMessageContaining("존재하지 않는 사용자입니다.");
    }

    @Test
    @DisplayName("작성자와 다른 사용자가 게시글을 수정하면 예외가 발생한다")
    public void updatePostInvalidMemberExceptionTest() throws Exception {
        //given
        PostUpdateRequest request = new PostUpdateRequest("afterTitle", "afterContent");
        Member newMember = new Member("newEmail", "newPassword");

        ReflectionTestUtils.setField(newMember, "id", 2L);

        // when, then
        when(postRepository.findPostWithMember(post1.getId())).thenReturn(Optional.ofNullable(post1));
        when(memberRepository.findByEmail(anyString())).thenReturn(Optional.of(newMember));

        assertThatThrownBy(
                () -> postService.update(post1.getId(), memberInfo, request))
                .isInstanceOf(InvalidMemberException.class)
                .hasMessageContaining("수정 권한이 없는 사용자입니다.");
    }

    @Test
    @DisplayName("게시글을 삭제한다")
    public void deletePostTest() throws Exception {
        //when
        when(postRepository.findPostWithMember(post1.getId())).thenReturn(Optional.ofNullable(post1));
        when(memberRepository.findByEmail(memberInfo.getUsername())).thenReturn(Optional.ofNullable(member));
        postService.delete(post1.getId(), memberInfo);

        //then
        verify(postRepository).delete(post1);
    }

    @Test
    @DisplayName("존재하지 않는 게시글을 삭제하면 예외가 발생한다")
    public void deletePostNoSuchPostExceptionTest() throws Exception {
        //given
        Long 존재하지_않는_게시글의_id = 1L;

        // when, then
        assertThatThrownBy(
                () -> postService.delete(존재하지_않는_게시글의_id, memberInfo))
                .isInstanceOf(NoSuchPostException.class)
                .hasMessageContaining("존재하지 않는 게시글입니다.");
    }

    @Test
    @DisplayName("존재하지 않는 사용자의 게시글을 삭제하면 예외가 발생한다")
    public void deletePostNoSuchMemberExceptionTest() throws Exception {
        // when, then
        when(postRepository.findPostWithMember(post1.getId())).thenReturn(Optional.ofNullable(post1));
        when(memberRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThatThrownBy(
                () -> postService.delete(post1.getId(), memberInfo))
                .isInstanceOf(NoSuchMemberException.class)
                .hasMessageContaining("존재하지 않는 사용자입니다.");
    }

    @Test
    @DisplayName("작성자와 다른 사용자가 게시글을 삭제하면 예외가 발생한다")
    public void deletePostInvalidMemberExceptionTest() throws Exception {
        //given
        Member newMember = new Member("newEmail", "newPassword");
        ReflectionTestUtils.setField(newMember, "id", 2L);

        // when, then
        when(postRepository.findPostWithMember(post1.getId())).thenReturn(Optional.ofNullable(post1));
        when(memberRepository.findByEmail(anyString())).thenReturn(Optional.of(newMember));

        assertThatThrownBy(
                () -> postService.delete(post1.getId(), memberInfo))
                .isInstanceOf(InvalidMemberException.class)
                .hasMessageContaining("삭제 권한이 없는 사용자입니다.");
    }
}
