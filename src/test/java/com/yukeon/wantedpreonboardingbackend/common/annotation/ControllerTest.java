package com.yukeon.wantedpreonboardingbackend.common.annotation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yukeon.wantedpreonboardingbackend.auth.application.CustomUserDetailsService;
import com.yukeon.wantedpreonboardingbackend.auth.jwt.JwtTokenProvider;
import com.yukeon.wantedpreonboardingbackend.member.application.MemberService;
import com.yukeon.wantedpreonboardingbackend.member.domain.Member;
import com.yukeon.wantedpreonboardingbackend.member.presentation.MemberController;
import com.yukeon.wantedpreonboardingbackend.member.util.MemberInfo;
import com.yukeon.wantedpreonboardingbackend.post.application.PostService;
import com.yukeon.wantedpreonboardingbackend.post.domain.Post;
import com.yukeon.wantedpreonboardingbackend.post.presentation.PostController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;


@WebMvcTest({
        PostController.class,
        MemberController.class,
        })
@AutoConfigureRestDocs
public abstract class ControllerTest {
    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    protected PostService postService;
    @MockBean
    protected MemberService memberService;

    @MockBean
    protected JwtTokenProvider jwtTokenProvider;

    @MockBean
    protected CustomUserDetailsService customUserDetailsService;

    protected Member member;
    protected MemberInfo memberInfo;
    protected Post post1;
    protected Post post2;
    protected Post post3;
}
