package com.myblog.security.oauth2.model;

import java.util.Map;

public abstract class OAuth2UserInfo {
    protected Map<String, Object> attributes;
    protected OAuth2UserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    /**
     * 소셜 계정 고유 식별자
     * @return id
     */
    public abstract Long getOAuth2Id();

    /**
     * 소셜 게정 이메일
     * @return email
     */
    public abstract String getEmail();

    /**
     * 소셜 계정 이름
     * @return name
     */
    public abstract String getName();

    public abstract OAuth2Provider getOAuth2Provider();

    public Map<String, Object> getAttributes() {
        return this.attributes;
    }
}
