package com.yukeon.wantedpreonboardingbackend.auth.application;

import com.yukeon.wantedpreonboardingbackend.member.domain.Member;
import com.yukeon.wantedpreonboardingbackend.member.exception.NoSuchMemberException;
import com.yukeon.wantedpreonboardingbackend.member.persistence.MemberRepository;
import com.yukeon.wantedpreonboardingbackend.member.util.MemberInfo;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;


import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CustomUserDetailsServiceTest {
    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;
    @Mock
    private MemberRepository memberRepository;

    @Test
    @DisplayName("사용자의 이메일이 주어지면 UserDetails 객체를 반환한다")
    public void loadUserByUsernameTest() throws Exception {
        //given
        String email = "yukeon97@gmail.com";
        String password = "12345678";
        Member member = new Member(email, password);

        //when
        when(memberRepository.findByEmail(email)).thenReturn(Optional.of(member));
        UserDetails actual = customUserDetailsService.loadUserByUsername(email);

        //then
        assertThat(actual).isInstanceOf(MemberInfo.class);
        assertThat(actual.getUsername()).isEqualTo(email);
        assertThat(actual.getPassword()).isEqualTo(password);
    }

    @Test
    @DisplayName("loadUserByUsername 실패 시 UsernameNotFoundException 발생")
    public void loadUserByUsernameFailureTest() {
        // given
        String 존재하지_않는_이메일 = "NotExistEmail@Null.Empty";

        // when, then
        assertThatThrownBy(
                () -> customUserDetailsService.loadUserByUsername(존재하지_않는_이메일))
                .isInstanceOf(NoSuchMemberException.class)
                .hasMessageContaining("존재하지 않는 사용자입니다.");
    }
}
