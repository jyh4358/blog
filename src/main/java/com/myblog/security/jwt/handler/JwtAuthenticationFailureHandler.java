package com.myblog.security.jwt.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myblog.common.dto.Response;
import com.myblog.security.jwt.JwtErrorCode;
import com.myblog.security.jwt.exception.JwtExpiredTokenException;
import com.myblog.security.jwt.exception.JwtModulatedTokenException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        System.out.println("JwtAuthenticationFailureHandler 호출");
        SecurityContextHolder.clearContext();
        Response<?> responseValue = null;
        if (exception instanceof JwtExpiredTokenException) {
            // 액세스 토큰 만료
            responseValue = Response.of(JwtErrorCode.ACCESS_TOKEN_EXPIRATION, null);
        } else if (exception instanceof JwtModulatedTokenException) {
            // 토큰 변조
            responseValue = Response.of(JwtErrorCode.TOKEN_MODULATED, null);
        }
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setHeader(HttpHeaders.CONTENT_ENCODING, "UTF-8");
        response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        if (Objects.nonNull(responseValue)) {
            PrintWriter writer = response.getWriter();
            writer.write(objectMapper.writeValueAsString(responseValue));
        }
    }
}
