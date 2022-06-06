package com.myblog.security.jwt.provider;

import com.myblog.security.token.JwtPreAuthenticationToken;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.MissingClaimException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {



    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String token = (String) authentication.getPrincipal();
        try {
            System.out.println("=========================");
            return null;

        } catch (Exception e) {
            throw new RuntimeException("변조된 JWT 토큰입니다.");

        }
    }

    // JwtPreAuthenticationToken 타입만 처리 가능하도록 설정
    @Override
    public boolean supports(Class<?> authentication) {
        return JwtPreAuthenticationToken.class.isAssignableFrom(authentication);
    }
}