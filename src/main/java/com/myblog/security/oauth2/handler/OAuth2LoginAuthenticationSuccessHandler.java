package com.myblog.security.oauth2.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myblog.article.dto.ArticleWriteDto;
import com.myblog.security.oauth2.CustomOauth2User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class OAuth2LoginAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        System.out.println("onAuthenticationSuccess 호출");

        Map<String, String> map = new HashMap<>();

        map.put("test", "test");

//        response.getWriter().write(objectMapper.writeValueAsString(map));
    }
}
