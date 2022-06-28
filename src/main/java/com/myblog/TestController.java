package com.myblog;

import com.myblog.category.dto.CategoryListDto;
import com.myblog.category.dto.CategoryQueryDto;
import com.myblog.category.resposiotry.CategoryRepository;
import com.myblog.category.service.CategoryService;
import com.myblog.security.oauth2.CustomOauth2User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class TestController {

    private final CategoryService categoryService;


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




    @GetMapping("/admin/articles")
    public String articles() {
        return "admin/article/articleList";
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

    @GetMapping("/admin/main")
    public String admin() {
        System.out.println("admin-----------------");
        return "admin/index";
    }

    @GetMapping("/test")
    public String test() {
        return "test";
    }


}
