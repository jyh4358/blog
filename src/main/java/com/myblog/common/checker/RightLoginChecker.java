package com.myblog.common.checker;

import com.myblog.common.exception.NonAuthenticationException;
import com.myblog.common.exception.NonLoginMemberException;
import com.myblog.member.model.Role;
import com.myblog.security.oauth2.model.CustomOauth2User;

public class RightLoginChecker {

    public static void checkLoginMember(CustomOauth2User customOauth2User) {
        if (customOauth2User == null) {
            throw new NonLoginMemberException();
        }
    }

    public static void checkAdminMember(CustomOauth2User customOauth2User) {
        if (customOauth2User == null) {
            throw new NonLoginMemberException();
        }
        if (!customOauth2User.getMemberRole().equals(Role.ADMIN)) {
            throw new NonAuthenticationException();
        }
    }
}
