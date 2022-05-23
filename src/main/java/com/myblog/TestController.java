package com.myblog;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestController {

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
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

}
