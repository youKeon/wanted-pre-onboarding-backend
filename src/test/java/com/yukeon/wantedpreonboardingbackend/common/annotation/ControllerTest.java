package com.yukeon.wantedpreonboardingbackend.common.annotation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yukeon.wantedpreonboardingbackend.member.application.MemberService;
import com.yukeon.wantedpreonboardingbackend.member.presentation.MemberController;
import com.yukeon.wantedpreonboardingbackend.post.application.PostService;
import com.yukeon.wantedpreonboardingbackend.post.presentation.PostController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
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
    protected MemberService memberService;
}
