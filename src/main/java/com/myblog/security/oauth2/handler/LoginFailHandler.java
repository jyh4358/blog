package com.myblog.security.oauth2.handler;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class LoginFailHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        String errMsg = "";

        if(exception instanceof OAuth2AuthenticationException){
            errMsg = "duplicatedEmail";
            request.setAttribute("errMsg", errMsg);
        }

        setDefaultFailureUrl("/login?error="+errMsg);
        super.onAuthenticationFailure(request, response, exception);
    }
}
