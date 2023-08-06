package com.yukeon.wantedpreonboardingbackend.common.annotation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yukeon.wantedpreonboardingbackend.auth.application.CustomUserDetailsService;
import com.yukeon.wantedpreonboardingbackend.auth.jwt.JwtFilter;
import com.yukeon.wantedpreonboardingbackend.auth.jwt.JwtTokenProvider;
import com.yukeon.wantedpreonboardingbackend.member.domain.Member;
import com.yukeon.wantedpreonboardingbackend.member.presentation.MemberController;
import com.yukeon.wantedpreonboardingbackend.member.util.MemberInfo;
import com.yukeon.wantedpreonboardingbackend.post.application.PostService;
import com.yukeon.wantedpreonboardingbackend.post.presentation.PostController;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@WebMvcTest({
        PostController.class,
        MemberController.class,
        })
@ActiveProfiles("test")
public abstract class ControllerTest {
    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    protected PostService postService;

    @MockBean
    protected JwtTokenProvider jwtTokenProvider;

    @MockBean
    protected CustomUserDetailsService customUserDetailsService;

    protected String ACCESS_TOKEN = "access_token";
}
