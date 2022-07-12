package com.myblog.commen;

import com.myblog.member.model.Member;
import com.myblog.member.model.Role;
import com.myblog.member.repository.MemberRepository;
import com.myblog.security.oauth2.model.CustomOauth2User;
import com.myblog.security.oauth2.model.GoogleUser;
import com.myblog.security.oauth2.model.OAuth2Provider;
import com.myblog.security.oauth2.model.OAuth2UserInfo;

import java.util.HashMap;
import java.util.Map;

public class TestHelper {

    public static Member 관리자_생성(MemberRepository memberRepository) {
        return memberRepository.save(Member.builder()
                .userId("관리자 계정")
                .username("관리자 이름")
                .email("test@gmail.com")
                .picUrl("")
                .provider(OAuth2Provider.GOOGLE)
                .role(Role.ADMIN)
                .build());
    }

    public static CustomOauth2User 인증_객체_생성(Member member) {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("email", "test@email.com");
        attributes.put("name", "관리자 이름");
        attributes.put("sub", "123");
        return new CustomOauth2User(new GoogleUser(attributes), member);
    }
}
