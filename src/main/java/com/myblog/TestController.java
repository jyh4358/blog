package com.myblog;

import com.myblog.member.model.Member;
import com.myblog.security.oauth2.CustomOauth2User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;

@Controller
public class TestController {

    @GetMapping("/")
    public String home() {

        return "index";
    }

    @GetMapping("/login")
    public String login(Model model) {
        String redirectUri = UriComponentsBuilder
                .fromUriString("https://accounts.google.com/o/oauth2/v2/auth")
                .queryParam("client_id", "428541390243-7cevccqe0afejrec8et1025hbk8v36p0.apps.googleusercontent.com")
                .queryParam("response_type", "code")
                .queryParam("scope", "email profile")
                .queryParam("redirect_uri", "http://localhost:8080/login/test")
                .toUriString();
        model.addAttribute("redirectUri", redirectUri);
        return "login";
    }
    @GetMapping("/login/test")
    public String loginTest(
            @AuthenticationPrincipal CustomOauth2User customOauth2User,
            @RequestParam String code, Model model) {
        model.addAttribute("code", code);


        return "loginSuccess";
    }

    @GetMapping("/article")
    public String article() {
        return "article/articleView";
    }


    @GetMapping("/admin/articles")
    public String articles() {
        return "admin/article/articleList";
    }

    @GetMapping("/admin/article")
    public String articlesWrite() {
        return "admin/article/articleWriteForm";
    }


    @GetMapping("/admin/categories")
    public String adminArticle() {
        return "admin/category/categoryList";
    }

    @GetMapping("/admin/category")
    public String categories() {
        return "admin/category/categoryAddForm";
    }

    @GetMapping("/admin/users")
    public String users() {
        return "admin/user/userList";
    }

    @GetMapping("/admin/user")
    public String user() {
        return "admin/user/userEditForm";
    }

    @GetMapping("/admin")
    public String admin() {
        return "admin/index";
    }

    @GetMapping("/test")
    public String test(Model model) {
        List<String> test1 = List.of("test1", "test2", "test3");
        TestList testList1 = new TestList(1L, "categoryTest1", test1);
        List<String> test2 = List.of("test11", "test22", "test33");
        TestList testList2 = new TestList(2L, "categoryTest2", test2);

        CategoryList categoryList = new CategoryList(List.of(testList1, testList2));

        model.addAttribute("categoryList", categoryList);

        return "test";
    }
}
