package com.myblog.article.controller;


import com.google.gson.Gson;
import com.myblog.article.dto.ArticleWriteDto;
import com.myblog.article.model.Article;
import com.myblog.article.repository.ArticleRepository;
import com.myblog.article.service.ArticleService;
import com.myblog.article.service.TagService;
import com.myblog.category.service.CategoryService;
import com.myblog.security.oauth2.CustomOauth2User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ArticleController {
    private final ArticleService articleService;
    private final CategoryService categoryService;
    private final TagService tagService;
    private final ArticleRepository articleRepository;

    @GetMapping("/admin/article")
    public String articleWriteForm(Model model) {

        model.addAttribute("categoryListDto", categoryService.findCategories());
        model.addAttribute("articleWriteDto", ArticleWriteDto.createDefaultArticleWriteDto());
//        model.addAttribute("tagsInput", collect);
        return "admin/article/articleWriteForm";
    }

    @PostMapping("/admin/article")
    public String articleWrite(
            ArticleWriteDto articleWriteDto,
            @AuthenticationPrincipal CustomOauth2User customOauth2User) {
        
        articleService.writeArticle(articleWriteDto, customOauth2User);
//        System.out.println("articleWriteDto = " + articleWriteDto);
//        System.out.println("customOauth2User.getMember() = " + customOauth2User.getMember());

        return "admin/article/articleList";
    }
}
