package com.myblog.common.checker;

import com.myblog.common.exception.NonLoginMemberException;
import com.myblog.security.oauth2.CustomOauth2User;

public class RightLoginChecker {

    public static void checkLoginMember(CustomOauth2User customOauth2User) {
        if (customOauth2User == null) {
            throw new NonLoginMemberException();
        }
    }
}
