package com.myblog.security.oauth2.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OAuth2Provider {
    KAKAO("kakao", "id"),
    NAVER("naver", "response"),
    GOOGLE("google", "sub");

    private String provier;
    private String attributeKey;


}
