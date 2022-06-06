package com.myblog.security.oauth2;

import com.myblog.security.oauth2.model.OAuth2UserInfo;
import com.myblog.member.model.Member;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import java.util.List;

@Getter
public class CustomOauth2User extends DefaultOAuth2User {
    private Member member;


    public CustomOauth2User(OAuth2UserInfo oAuth2UserInfo, Member member) {
        super(List.of(new SimpleGrantedAuthority(member.getRole().getDesc())), oAuth2UserInfo.getAttributes(), member.getProvider().getAttributeKey());
        this.member = member;
    }

    // 시큐리티 컨텍스트 내의 인증 정보를 가져와 하는 작업을 수행할 경우 계정 식별자가 사용되도록 조치
    @Override
    public String getName() {
        return member.getUserId();
    }
}