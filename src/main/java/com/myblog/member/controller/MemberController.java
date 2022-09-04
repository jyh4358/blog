package com.myblog.member.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MemberController {

    /*
        - 로그인 페이지 요청
     */
    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
