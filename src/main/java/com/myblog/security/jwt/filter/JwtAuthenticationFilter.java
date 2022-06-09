package com.myblog.security.jwt.filter;

import com.myblog.security.token.JwtPreAuthenticationToken;
import org.aspectj.weaver.patterns.IToken;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class JwtAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private static final String AUTHORIZATION = "Authorization";


    public JwtAuthenticationFilter(RequestMatcher requestmatcher) {
        super(requestmatcher);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        System.out.println("JwtAuthenticationFilter successfulAuthentication 호출");
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authResult);
        SecurityContextHolder.setContext(context);
        chain.doFilter(request, response);

    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        System.out.println("JwtAuthenticationFilter attemptAuthentication 호출");
        String token = request.getHeader(AUTHORIZATION);

        if (Objects.isNull(token)) {
            System.out.println(" ============1111======= ");
            return new AnonymousAuthenticationToken(UUID.randomUUID().toString(),
                    "anonymous",
                    List.of(new SimpleGrantedAuthority("ROLE_USER")));
        }
            System.out.println(" ============2222======= ");
        JwtPreAuthenticationToken preAuthenticationToken = new JwtPreAuthenticationToken(token);
        return this.getAuthenticationManager().authenticate(preAuthenticationToken);
    }
}
