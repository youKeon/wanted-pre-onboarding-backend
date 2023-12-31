package com.yukeon.wantedpreonboardingbackend.post.presentation;

import com.yukeon.wantedpreonboardingbackend.common.annotation.ControllerTest;
import com.yukeon.wantedpreonboardingbackend.member.domain.Member;
import com.yukeon.wantedpreonboardingbackend.member.util.MemberInfo;
import com.yukeon.wantedpreonboardingbackend.post.domain.Post;
import com.yukeon.wantedpreonboardingbackend.post.dto.request.PostCreateRequest;
import com.yukeon.wantedpreonboardingbackend.post.dto.request.PostUpdateRequest;
import com.yukeon.wantedpreonboardingbackend.post.dto.response.PostResponse;
import com.yukeon.wantedpreonboardingbackend.post.dto.response.PostsResponse;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class PostControllerTest extends ControllerTest {
    private static final String baseURL = "/api/v1/posts";
    private static Pageable pageable = PageRequest.of(0, 3);

    @BeforeEach
    void setup() {
        member = new Member("yukeon97@gmail.com", "12345678");
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
    @WithMockUser
    @DisplayName("인증된 사용자는 게시글을 단건 조회할 수 있다")
    public void findPostByAuthorizedMemberTest() throws Exception {
        //given
        PostResponse response = new PostResponse("title", "content");

        //when
        when(postService.find(post1.getId())).thenReturn(response);

        //then
        mockMvc.perform(get(baseURL + "/{id}", post1.getId()))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("post/find/success",
                        pathParameters(
                                parameterWithName("id").description("게시글 ID")
                        ),
                        responseFields(
                                fieldWithPath("title").description("제목"),
                                fieldWithPath("content").description("내용")
                        )
                ));
    }

    @Test
    @WithAnonymousUser
    @DisplayName("인증되지 않은 사용자는 게시글을 단건 조회할 수 없다")
    public void findPostByUnauthorizedMemberTest() throws Exception {
        //given
        PostResponse response = new PostResponse("title", "content");

        //when
        when(postService.find(post1.getId())).thenReturn(response);

        //then
        mockMvc.perform(get(baseURL + "/{id}", post1.getId()))
                .andExpect(status().isUnauthorized())
                .andDo(document("post/find/fail/unauthorizedMember",
                        pathParameters(
                                parameterWithName("id").description("게시글 ID")
                        )
                ));
    }

    @Test
    @WithMockUser
    @DisplayName("인증된 사용자는 게시글을 생성할 수 있다")
    public void createPostByAuthorizedMember() throws Exception {
        //given
        PostCreateRequest request = new PostCreateRequest("Title", "Content");

        //then
        mockMvc.perform(post(baseURL)
                        .with(csrf())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("post/create/success",
                        requestFields(
                                fieldWithPath("title").description("제목"),
                                fieldWithPath("content").description("내용")
                        )
                ));
    }

    @Test
    @WithAnonymousUser
    @DisplayName("인증되지 않은 사용자는 게시글을 생성할 수 없다")
    public void createPostByUnauthorizedMember() throws Exception {
        //given
        PostCreateRequest request = new PostCreateRequest("Title", "Content");

        //then
        mockMvc.perform(post(baseURL)
                        .with(csrf())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andDo(print())
                .andDo(document("post/create/fail/unauthorizedMember",
                        requestFields(
                                fieldWithPath("title").description("제목"),
                                fieldWithPath("content").description("내용")
                        )
                ));
    }

    @Test
    @WithMockUser
    @DisplayName("제목이 공백인 게시글을 생성할 수 없다")
    public void createPostEmptyTitleException() throws Exception {
        //given
        PostCreateRequest request = new PostCreateRequest("", "Content");

        //then
        mockMvc.perform(post(baseURL)
                        .with(csrf())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andDo(document("post/create/fail/emptyTitle",
                        requestFields(
                                fieldWithPath("title").description("제목"),
                                fieldWithPath("content").description("내용")
                        ),
                        responseFields(
                                fieldWithPath("message").description("응답 메세지")
                        )
                ));;
    }

    @Test
    @WithMockUser
    @DisplayName("내용이 공백인 게시글을 생성할 수 없다")
    public void createPostEmptyContentException() throws Exception {
        //given
        PostCreateRequest request = new PostCreateRequest("Ttitle", "");

        //then
        mockMvc.perform(post(baseURL)
                        .with(csrf())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andDo(document("post/create/fail/emptyContent",
                        requestFields(
                                fieldWithPath("title").description("제목"),
                                fieldWithPath("content").description("내용")
                        ),
                        responseFields(
                                fieldWithPath("message").description("응답 메세지")
                        )
                ));;
    }

    @Test
    @WithMockUser
    @DisplayName("인증된 사용자는 게시글을 수정할 수 있다")
    public void updatePostByAuthorizedMember() throws Exception {
        //given
        PostUpdateRequest request = new PostUpdateRequest("updateTitle", "updateContent");

        //when, then
        mockMvc.perform(put(baseURL + "/{id}", post1.getId())
                        .with(csrf())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("post/update/success",
                        pathParameters(
                                parameterWithName("id").description("게시글 ID")
                        ),
                        requestFields(
                                fieldWithPath("title").description("제목"),
                                fieldWithPath("content").description("내용")
                        )
                ));
    }

    @Test
    @WithMockUser
    @DisplayName("게시글 수정 시 제목이 공백이면 예외가 발생한다")
    public void updatePostEmptyTitleException() throws Exception {
        //given
        PostUpdateRequest request = new PostUpdateRequest("", "Content");

        //then
        mockMvc.perform(put(baseURL + "/{id}", post1.getId())
                        .with(csrf())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andDo(document("post/update/fail/emptyTitle",
                        requestFields(
                                fieldWithPath("title").description("제목"),
                                fieldWithPath("content").description("내용")
                        ),
                        responseFields(
                                fieldWithPath("message").description("응답 메세지")
                        )
                ));;
    }

    @Test
    @WithMockUser
    @DisplayName("게시글 수정 시 내용이 공백이면 예외가 발생한다")
    public void updatePostEmptyContentException() throws Exception {
        //given
        PostUpdateRequest request = new PostUpdateRequest("Title", "");

        //then
        mockMvc.perform(put(baseURL + "/{id}", post1.getId())
                        .with(csrf())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andDo(document("post/update/fail/emptyTitle",
                        requestFields(
                                fieldWithPath("title").description("제목"),
                                fieldWithPath("content").description("내용")
                        ),
                        responseFields(
                                fieldWithPath("message").description("응답 메세지")
                        )
                ));;
    }
    @Test
    @WithAnonymousUser
    @DisplayName("인증되지 않은 사용자는 게시글을 수정할 수 없다")
    public void updatePostByUnauthorizedMember() throws Exception {
        //given
        PostUpdateRequest request = new PostUpdateRequest("updateTitle", "updateContent");

        //when, then
        mockMvc.perform(put(baseURL + "/{id}", post1.getId())
                        .with(csrf())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andDo(print())
                .andDo(document("post/update/fail/unauthorizedMember",
                        pathParameters(
                                parameterWithName("id").description("게시글 ID")
                        ),
                        requestFields(
                                fieldWithPath("title").description("제목"),
                                fieldWithPath("content").description("내용")
                        )
                ));
    }

    @Test
    @WithMockUser
    @DisplayName("인증된 사용자는 게시글을 삭제할 수 있다")
    public void deletePostByAuthorizedMember() throws Exception {
        //when, then
        mockMvc.perform(delete(baseURL + "/{id}", post1.getId())
                .with(csrf()))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("post/delete/success",
                        pathParameters(
                                parameterWithName("id").description("게시글 ID")
                        )
                ));
    }

    @Test
    @WithAnonymousUser
    @DisplayName("인증되지 않은 사용자는 게시글을 삭제할 수 없다")
    public void deletePostByUnauthorizedMember() throws Exception {
        //when, then
        mockMvc.perform(delete(baseURL + "/{id}", post1.getId())
                        .with(csrf()))
                .andExpect(status().isUnauthorized())
                .andDo(print())
                .andDo(document("post/delete/fail/unauthorizedMember",
                        pathParameters(
                                parameterWithName("id").description("게시글 ID")
                        )
                ));
    }

    @Test
    @WithMockUser
    @DisplayName("인증된 사용자는 게시글을 조회할 수 있다")
    public void findAllPostByAuthorizedMember() throws Exception {
        // given
        int page = 1;
        int size = 10;

        Page<Post> postPage = new PageImpl<>(List.of(post1, post2, post3), pageable, 3);
        when(postService.findAll(page - 1, size)).thenReturn(PostsResponse.of(postPage));

        // when, then
        mockMvc.perform(get(baseURL)
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numberOfPosts").value(postPage.getTotalElements()))
                .andExpect(jsonPath("$.isLastPage").value(postPage.isLast()))
                .andExpect(jsonPath("$.postInfoResponses", Matchers.hasSize(3)))
                .andDo(print())
                .andDo(document("post/findAll/success",
                        requestParameters(
                                parameterWithName("page").description("페이지"),
                                parameterWithName("size").description("사이즈")
                        )
                ));
    }

    @Test
    @WithAnonymousUser
    @DisplayName("인증되지 않은 사용자는 게시글을 조회할 수 없다")
    public void findAllPostByUnauthorizedMember() throws Exception {
        // given
        int page = 1;
        int size = 10;

        // when, then
        mockMvc.perform(get(baseURL)
                .param("page", String.valueOf(page))
                .param("size", String.valueOf(size)))
                .andExpect(status().isUnauthorized())
                .andDo(print())
                .andDo(document("post/findAll/success",
                        requestParameters(
                                parameterWithName("page").description("페이지"),
                                parameterWithName("size").description("사이즈")
                        )
                ));
    }
}
