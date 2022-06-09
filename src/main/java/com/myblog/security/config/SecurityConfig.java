package com.myblog.security.config;

import com.myblog.security.jwt.filter.FilterSkipMatcher;
import com.myblog.security.jwt.filter.JwtAuthenticationFilter;
import com.myblog.security.jwt.handler.JwtAuthenticationFailureHandler;
import com.myblog.security.jwt.provider.JwtAuthenticationProvider;
import com.myblog.security.oauth2.handler.OAuth2LoginAuthenticationSuccessHandler;
import com.myblog.security.oauth2.service.Oauth2MemberService;
import com.myblog.member.model.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.servlet.Filter;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final Oauth2MemberService oauth2MemberService;

    private final OAuth2LoginAuthenticationSuccessHandler oAuth2LoginAuthenticationSuccessHandler;


    private final JwtAuthenticationProvider jwtAuthenticationProvider;

    private final JwtAuthenticationFailureHandler jwtAuthenticationFailureHandler;

    @Bean
    public BCryptPasswordEncoder encodePwd() {
        return new BCryptPasswordEncoder();
    }

    public Filter jwtAuthenticationFilter() throws Exception {
        FilterSkipMatcher filterSkipMatcher = new FilterSkipMatcher(
                List.of("/api/refresh", "/login", "/oauth/google/callback"),
                List.of("/**")
        );
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(filterSkipMatcher);
        jwtAuthenticationFilter.setAuthenticationManager(super.authenticationManager());
        jwtAuthenticationFilter.setAuthenticationFailureHandler(jwtAuthenticationFailureHandler);
        return jwtAuthenticationFilter;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        super.configure(auth);
        auth.authenticationProvider(jwtAuthenticationProvider);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .formLogin().disable()
                .httpBasic().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .authorizeRequests()
                .antMatchers("/admin/**").hasRole(Role.ADMIN.name())
                .antMatchers("/").hasRole(Role.USER.name())
                .antMatchers("/comment").hasRole(Role.USER.name())
                .anyRequest().permitAll()

                .and()
                .logout()
                .logoutSuccessUrl("/")

                .and()
                .addFilterBefore(jwtAuthenticationFilter(), OAuth2AuthorizationRequestRedirectFilter.class)
                .oauth2Login()
                .loginPage("/login")
                .successHandler(oAuth2LoginAuthenticationSuccessHandler)
                .userInfoEndpoint()
                .userService(oauth2MemberService);


    }
}
