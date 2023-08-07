package com.yukeon.wantedpreonboardingbackend.auth.jwt;

import com.yukeon.wantedpreonboardingbackend.auth.application.CustomUserDetailsService;
import com.yukeon.wantedpreonboardingbackend.member.domain.Member;
import com.yukeon.wantedpreonboardingbackend.member.exception.NoSuchMemberException;
import com.yukeon.wantedpreonboardingbackend.member.util.MemberInfo;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JwtTokenProviderTest {
    private static final String JWT_SECRET_KEY = "ABC".repeat(32);
    private static final long JWT_EXPIRE_LENGTH = 3600;
    private static final String EMAIL = "yukeon97@example.com";
    private static final String PASSWORD = "12345678";
    @Mock
    private CustomUserDetailsService customUserDetailsService;
    @InjectMocks
    private JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(JWT_EXPIRE_LENGTH, JWT_SECRET_KEY, customUserDetailsService);

    @DisplayName("토큰을 생성한다")
    @Test
    void generateTokenTest() {
        // given
        String actual = jwtTokenProvider.generateToken(EMAIL);

        // when, then
        assertThat(actual.split("\\.")).hasSize(3);
    }

    @DisplayName("토큰을 검증하며 만료된 경우 false 반환한다.")
    @Test
    void inValidateTokenWhenExpired() {
        // given
        JwtTokenProvider expiredJwtTokenProvider = new JwtTokenProvider(0, JWT_SECRET_KEY, customUserDetailsService);

        // when
        String expiredToken = expiredJwtTokenProvider.generateToken(EMAIL);

        // then
        assertThat(expiredJwtTokenProvider.validateToken(expiredToken)).isFalse();
    }

    @DisplayName("토큰을 검증하며 유효하지 않으면 false 반환한다.")
    @Test
    void inValidateTokenWhenInvalid() {
        // given
        String invalidedToken = "invalidedToken";

        // when, then
        assertThat(jwtTokenProvider.validateToken(invalidedToken)).isFalse();
    }

    @DisplayName("토큰에서 Authentication 객체를 반환한다.")
    @Test
    void getAuthenticationFromToken() {
        // given
        String token = jwtTokenProvider.generateToken(EMAIL);
        Member member = new Member(EMAIL, PASSWORD);
        MemberInfo memberInfo = new MemberInfo(member);

        // when
        when(customUserDetailsService.loadUserByUsername(EMAIL)).thenReturn(memberInfo);
        Authentication authentication = jwtTokenProvider.getAuthentication(token);

        // then
        assertThat(authentication).isNotNull();
        assertThat(authentication.getName()).isEqualTo(memberInfo.getUsername());
        assertThat(authentication.getPrincipal()).isInstanceOf(UserDetails.class);
    }


    @DisplayName("유효한 사용자가 없는 경우 UsernameNotFoundException 발생")
    @Test
    void throwUsernameNotFoundExceptionWhenUserNotFound() {
        String 유효하지_않은_이메일 = "invalid@example.com";
        when(customUserDetailsService.loadUserByUsername(유효하지_않은_이메일)).thenThrow(new NoSuchMemberException());

        String token = jwtTokenProvider.generateToken(유효하지_않은_이메일);

        assertThatThrownBy(() -> jwtTokenProvider.getAuthentication(token))
                .isInstanceOf(NoSuchMemberException.class)
                .hasMessageContaining("존재하지 않는 사용자입니다.");
    }
}
