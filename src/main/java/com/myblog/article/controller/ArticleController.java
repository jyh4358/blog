package com.myblog.article.controller;


import com.myblog.article.dto.ArticleCardBoxResponse;
import com.myblog.article.dto.ArticleDetailResponse;
import com.myblog.article.dto.ArticleWriteDto;
import com.myblog.article.dto.PageDto;
import com.myblog.article.service.ArticleService;
import com.myblog.article.service.TagService;
import com.myblog.category.service.CategoryService;
import com.myblog.comment.dto.CommentListResponse;
import com.myblog.comment.service.CommentService;
import com.myblog.security.oauth2.CustomOauth2User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ArticleController {
    private final ArticleService articleService;
    private final CategoryService categoryService;
    private final CommentService commentService;
    private final TagService tagService;

    @GetMapping("/")
    public String index(Model model) {

        model.addAttribute("popularArticleResponse", articleService.findPopularArticle());

        return "index";
    }

    @GetMapping("/admin/article-write")
    public String articleWriteForm(Model model) {


        model.addAttribute("tagListDto", tagService.findAllTag());
        model.addAttribute("categoryListDto", categoryService.findCategories());
        model.addAttribute("articleWriteDto", ArticleWriteDto.createDefaultArticleWriteDto());

//        model.addAttribute("tagsInput", collect);
        return "admin/article/articleWriteForm";
    }

    @PostMapping("/admin/article")
    public String articleWrite(
            ArticleWriteDto articleWriteDto,
            @AuthenticationPrincipal CustomOauth2User customOauth2User
    ) {
        
        articleService.writeArticle(articleWriteDto, customOauth2User);
        System.out.println("articleWriteDto = " + articleWriteDto);
//        System.out.println("customOauth2User.getMember() = " + customOauth2User.getMember());

        return "admin/article/articleList";
    }
    @GetMapping("/article/{articleId}")
    public String articleView(
            @PathVariable Long articleId,
            @AuthenticationPrincipal CustomOauth2User customOauth2User,
            @CookieValue(required = false, name = "hit") String hitCookieValue,
            Model model,
            HttpServletResponse response
    ) {
        boolean hitCheck = checkDuplicateHitCount(articleId, hitCookieValue, response);

        List<CommentListResponse> commentList = commentService.findCommentList(articleId);
        ArticleDetailResponse articleDetailResponse = articleService.findArticleDetail(articleId, hitCheck);
        System.out.println("articleDetailResponse = " + articleDetailResponse);
        model.addAttribute("articleDetailResponse", articleDetailResponse);
        model.addAttribute("commentList", commentList);

        return "article/articleView";
    }


    @GetMapping("/article")
    public String findSearchArticle(
            @PageableDefault(size = 8, sort = "id",direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam String categoryTitle,
            Model model
    ) {
//        List<ArticleCardBoxResponse> articleCardBoxList = articleService.findArticleAll(pageable);
        Page<ArticleCardBoxResponse> articleAll = articleService.findSearchArticle(categoryTitle, pageable);
        model.addAttribute("articleCardBoxList", articleAll);
        model.addAttribute("pageDto", PageDto.of(articleAll));
        List<ArticleCardBoxResponse> content = articleAll.getContent();
        System.out.println("content = " + content);
        System.out.println("articleAll = " + articleAll.getTotalElements());
        System.out.println("============================================================");
        return "article/articleList";
    }

//    @GetMapping("/admin/api/article")
//    @ResponseBody
//    public PageDto findArticleAll1(
//            @PageableDefault(
//                    size = 5, sort = "id",direction = Sort.Direction.DESC) Pageable pageable) {
////        List<ArticleCardBoxResponse> articleCardBoxList = articleService.findArticleAll(pageable);
//        Page<ArticleCardBoxResponse> articleAll = articleService.findArticleAll(pageable);
//        List<ArticleCardBoxResponse> content = articleAll.getContent();
//        System.out.println("content = " + content);
//        System.out.println("============================================================");
//        return PageDto.of(articleAll);
//    }

    /*
        조회 수 중복 방지 로직
     */
    private boolean checkDuplicateHitCount(Long articleId, String hitCookieValue, HttpServletResponse response) {

        if (!StringUtils.hasText(hitCookieValue)) {
            Cookie hitCookie = new Cookie("hit", articleId + "/");
            hitCookie.setMaxAge(60 * 30);
            response.addCookie(hitCookie);
            return true;
        }

        String[] viewCookieList = hitCookieValue.split("/");
        for (String s : viewCookieList) {
            System.out.println("cookieviewview = " + s);
        }
        if (Arrays.stream(viewCookieList).anyMatch(s -> s.equals(articleId.toString()))) {
            return false;
        } else {
            hitCookieValue += articleId + "/";
            response.addCookie(new Cookie("hit", hitCookieValue));
            return true;
        }
    }
}
