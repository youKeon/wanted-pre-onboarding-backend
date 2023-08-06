package com.yukeon.wantedpreonboardingbackend.member.application;

import com.yukeon.wantedpreonboardingbackend.auth.jwt.JwtTokenProvider;
import com.yukeon.wantedpreonboardingbackend.member.domain.Member;
import com.yukeon.wantedpreonboardingbackend.member.dto.request.MemberSignInRequest;
import com.yukeon.wantedpreonboardingbackend.member.dto.request.MemberSignUpRequest;
import com.yukeon.wantedpreonboardingbackend.member.dto.response.MemberSignInResponse;
import com.yukeon.wantedpreonboardingbackend.member.persistence.MemberRepository;
import com.yukeon.wantedpreonboardingbackend.member.util.MemberInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
@Transactional
public class MemberServiceTest {
    @InjectMocks
    private MemberService memberService;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtTokenProvider jwtTokenProvider;

    private Member member;
    private MemberInfo memberInfo;
    @BeforeEach
    void setup() {
        member = new Member("yukeon97@gmail.com", "12345678");
        memberInfo = new MemberInfo(member);

        ReflectionTestUtils.setField(member, "id", 1L);
    }
    
    @Test
    @DisplayName("회원가입을 할 수 있다")
    public void signUpTest() throws Exception {
        //given
        MemberSignUpRequest request = new MemberSignUpRequest(member.getEmail(), member.getPassword());
        String encodedPassword = "encodedPassword";

        //when
        when(passwordEncoder.encode(member.getPassword())).thenReturn(encodedPassword);
        when(memberRepository.save(any(Member.class))).thenReturn(null);
        memberService.signUp(request);

        //then
        verify(passwordEncoder, times(1)).encode(member.getPassword());
        verify(memberRepository, times(1)).save(any(Member.class));
    }

    @Test
    @DisplayName("로그인을 할 수 있다")
    public void signInTest() throws Exception {
        //given
        MemberSignInRequest request = new MemberSignInRequest(member.getEmail(), member.getPassword());
        String accessToken = "accessToken";

        //when
        when(jwtTokenProvider.generateToken(member.getEmail())).thenReturn(accessToken);
        MemberSignInResponse response = memberService.signIn(request);

        //then
        assertThat(response.getAccessToken()).isEqualTo(accessToken);
    }
}
