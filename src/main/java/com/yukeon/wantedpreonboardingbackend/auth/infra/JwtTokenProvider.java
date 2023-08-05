package com.yukeon.wantedpreonboardingbackend.auth.infra;

import com.yukeon.wantedpreonboardingbackend.auth.application.CustomUserDetailsService;
import com.yukeon.wantedpreonboardingbackend.auth.dto.response.TokenInfoResponse;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;


@Component
@Slf4j
public class JwtTokenProvider {

    private final long tokenExpiredTime; // 30분
    private final Key key;
    private CustomUserDetailsService customUserDetailsService;


    public JwtTokenProvider(@Value("${security.jwt.access.expire-length}") long tokenExpiredTime,
                            @Value("${security.jwt.access.secret-key}") String tokenSecretKey,
                            CustomUserDetailsService customUserDetailsService) {

        this.tokenExpiredTime = tokenExpiredTime;
        this.key = Keys.hmacShaKeyFor(tokenSecretKey.getBytes(StandardCharsets.UTF_8));
        this.customUserDetailsService = customUserDetailsService;
    }

    public TokenInfoResponse generateToken(Authentication authentication) {
        return generateToken(authentication.getName(), authentication.getAuthorities());
    }

    public TokenInfoResponse generateToken(String name, Collection<? extends GrantedAuthority> inputAuthorities) {
        String authorities = inputAuthorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = (new Date()).getTime();

        // Access Token 생성
        Date accessTokenExpiresIn = new Date(now + tokenExpiredTime);
        String accessToken = Jwts.builder()
                .setSubject(name)
                .claim("auth", authorities)
                .setExpiration(accessTokenExpiresIn)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();

        // Refresh Token 생성
        String refreshToken = Jwts.builder()
                .setExpiration(new Date(now + tokenExpiredTime))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();

        return TokenInfoResponse.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public Authentication getAuthentication(String accessToken) {
        // 토큰 복호화
        Claims claims = parseClaims(accessToken);

        if (claims.get("auth") == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        // 클레임에서 권한 정보 가져오기
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get("auth").toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        // UserDetails 객체를 만들어서 Authentication 리턴
        UserDetails principal = customUserDetailsService.loadUserByUsername(claims.getSubject());

        return new UsernamePasswordAuthenticationToken(principal, "", principal.getAuthorities());
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }

    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    public Long getExpiration(String accessToken) {
        //accessToken 남은 유효시간
        Date expiration = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody().getExpiration();
        return (expiration.getTime() - System.currentTimeMillis());
    }
}