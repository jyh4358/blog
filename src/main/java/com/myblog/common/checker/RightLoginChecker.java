package com.myblog.common.checker;

import com.myblog.member.model.Role;
import com.myblog.security.oauth2.model.CustomOauth2User;

import static com.myblog.common.exception.ExceptionMessage.INVALID_AUTHORIZATION;
import static com.myblog.common.exception.ExceptionMessage.INVALID_LOGIN;

public class RightLoginChecker {

    public static void checkLoginMember(CustomOauth2User customOauth2User) {
        if (customOauth2User == null) {
            throw INVALID_LOGIN.getException();
        }
    }

    public static void checkAdminMember(CustomOauth2User customOauth2User) {
        if (customOauth2User == null) {
            throw INVALID_LOGIN.getException();
        }
        if (!customOauth2User.getMemberRole().equals(Role.ADMIN)) {
            throw INVALID_AUTHORIZATION.getException();
        }
    }
}
