package com.yukeon.wantedpreonboardingbackend.member.presentation;

import com.yukeon.wantedpreonboardingbackend.common.annotation.ControllerTest;
import com.yukeon.wantedpreonboardingbackend.member.domain.Member;
import com.yukeon.wantedpreonboardingbackend.member.dto.request.MemberSignInRequest;
import com.yukeon.wantedpreonboardingbackend.member.dto.request.MemberSignUpRequest;
import com.yukeon.wantedpreonboardingbackend.member.dto.response.MemberSignInResponse;
import com.yukeon.wantedpreonboardingbackend.member.util.MemberInfo;
import com.yukeon.wantedpreonboardingbackend.post.domain.Post;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
public class MemberControllerTest extends ControllerTest {
    private static final String baseURL = "/api/v1/members";

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
    @DisplayName("회원가입을 할 수 있다")
    public void signUpTest() throws Exception {
        // given
        MemberSignUpRequest request = new MemberSignUpRequest(member.getEmail(), member.getPassword());

        // when, then
        mockMvc.perform(post(baseURL + "/signup")
                        .with(csrf())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    @DisplayName("잘못된 이메일 형식으로 회원가입을 할 수 없다")
    public void signUpWithInvalidEmailFormatTest() throws Exception {
        // given
        MemberSignUpRequest request = new MemberSignUpRequest("이메에에에에일", member.getPassword());

        // when, then
        mockMvc.perform(post(baseURL + "/signup")
                        .with(csrf())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    @DisplayName("비밀번호가 8글자 이상이 안 되면 회원가입을 할 수 없다")
    public void signUpWithInvalidPasswordFormatTest() throws Exception {
        // given
        MemberSignUpRequest request = new MemberSignUpRequest(member.getEmail(), "123");

        // when, then
        mockMvc.perform(post(baseURL + "/signup")
                        .with(csrf())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    @DisplayName("비밀번호가 공백이면 회원가입을 할 수 없다")
    public void signUpWithEmptyPasswordFormatTest() throws Exception {
        // given
        MemberSignUpRequest request = new MemberSignUpRequest(member.getEmail(), "");

        // when, then
        mockMvc.perform(post(baseURL + "/signup")
                        .with(csrf())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    @DisplayName("로그인을 할 수 있다")
    public void signInTest() throws Exception {
        // given
        MemberSignInRequest request = new MemberSignInRequest(member.getEmail(), member.getPassword());
        MemberSignInResponse response = new MemberSignInResponse("accessToken");

        // when, then
        when(memberService.signIn(request)).thenReturn(response);
        mockMvc.perform(post(baseURL + "/signin")
                        .with(csrf())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    @DisplayName("잘못된 이메일 형식으로 로그인 할 수 없다")
    public void signInWithInvalidEmailFormatTest() throws Exception {
        // given
        MemberSignInRequest request = new MemberSignInRequest("이메에에에에일", member.getPassword());

        // when, then
        mockMvc.perform(post(baseURL + "/signin")
                        .with(csrf())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    @DisplayName("비밀번호가 8글자 이상이 안 되면 로그인을 할 수 없다")
    public void signInWithInvalidPasswordFormatTest() throws Exception {
        // given
        MemberSignInRequest request = new MemberSignInRequest(member.getEmail(), "123");

        // when, then
        mockMvc.perform(post(baseURL + "/signin")
                        .with(csrf())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    @DisplayName("비밀번호가 공백이면 로그인을 할 수 없다")
    public void signInWithEmptyPasswordFormatTest() throws Exception {
        // given
        MemberSignInRequest request = new MemberSignInRequest(member.getEmail(), "");

        // when, then
        mockMvc.perform(post(baseURL + "/signin")
                        .with(csrf())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
