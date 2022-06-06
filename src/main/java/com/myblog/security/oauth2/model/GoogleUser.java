package com.myblog.security.oauth2.model;

import java.util.Map;


public class GoogleUser extends OAuth2UserInfo{

    private String googleEmail;
    private String googleName;

    public GoogleUser(Map<String, Object> attributes) {
        super(attributes);
        this.googleEmail = attributes.get("email").toString();
        this.googleName = attributes.get("name").toString();
    }

    @Override
    public Long getOAuth2Id() {
        return null;
    }

    @Override
    public String getEmail() {
        return this.googleEmail;
    }

    @Override
    public String getName() {
        return this.googleName;
    }

    @Override
    public OAuth2Provider getOAuth2Provider() {
        return OAuth2Provider.GOOGLE;
    }
}
