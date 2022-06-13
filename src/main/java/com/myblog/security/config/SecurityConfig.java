package com.myblog.security.config;

import com.myblog.member.model.Role;
import com.myblog.security.oauth2.handler.LoginFailHandler;
import com.myblog.security.oauth2.service.Oauth2MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final Oauth2MemberService oauth2MemberService;
    private final LoginFailHandler loginFailHandler;


    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf()
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .and()
                .formLogin().disable()
                .httpBasic().disable()

                .authorizeRequests()
                .antMatchers("/admin/**").hasRole(Role.ADMIN.name())
//                .antMatchers("/").hasRole(Role.USER.name())
                .antMatchers("/comment").hasRole(Role.USER.name())
                .anyRequest().permitAll()

                .and()
                .logout()
                .logoutSuccessUrl("/")

                .and()
                .csrf()
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())

                .and()
                .oauth2Login()
                .loginPage("/login")
                .failureHandler(loginFailHandler)
                .userInfoEndpoint()
                .userService(oauth2MemberService);


    }
}
