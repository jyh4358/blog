package com.myblog.article.controller;


import com.myblog.article.dto.*;
import com.myblog.article.service.ArticleService;
import com.myblog.article.service.TagService;
import com.myblog.category.service.CategoryService;
import com.myblog.comment.dto.CommentListResponse;
import com.myblog.comment.service.CommentService;
import com.myblog.common.checker.RightLoginChecker;
import com.myblog.security.oauth2.model.CustomOauth2User;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpStatus;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
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

    /**
     * 메인 페이지 요청
     * @model - popularArticleResponse : 인기 게시물
     * @return
     */
    @GetMapping("/")
    public String index(Model model) {

        model.addAttribute("popularArticleResponse", articleService.findPopularArticle());

        return "index";
    }

    /**
     * 게시물 작성 페이지 요청
     * @model - tagListDto : tag list
     *        - categoryListDto : category list
     * @return
     */
    @GetMapping("/admin/article-write")
    public String articleWriteForm(
            Model model,
            @AuthenticationPrincipal CustomOauth2User customOauth2User
    ) {
        RightLoginChecker.checkAdminMember(customOauth2User);
        model.addAttribute("tagListDto", tagService.findAllTag());
        model.addAttribute("categoryListDto", categoryService.findCategories());
//        model.addAttribute("articleWriteDto", ArticleWriteDto.createDefaultArticleDto());

        return "admin/article/articleWriteForm";
    }


    /**
     * 게시물 수정 페이지 요청
     * @param articleId - article 식별자
     * @model - articleModifyResponse : article detail dto
     *        - tagListDto : tag list
     *        - categoryListDto : category list
     * @return
     */
    @GetMapping("/admin/article-modify/{articleId}")
    public String modifyArticleForm(
            Model model,
            @PathVariable Long articleId,
            @AuthenticationPrincipal CustomOauth2User customOauth2User
            ) {
        ArticleModifyResponse articleModifyResponse = articleService.findModifyArticle(articleId, customOauth2User);

        model.addAttribute("articleModifyResponse", articleModifyResponse);
        model.addAttribute("tagListDto", tagService.findAllTag());
        model.addAttribute("categoryListDto", categoryService.findCategories());

        return "admin/article/articleModifyForm";
    }


    /**
     * 게시물 상세 페이지 요청
     * @param articleId : article 식별자
     * @param hitCookieValue : 중복 조회 방지 cookie value
     * @model - articleDetailResponse : article detail dto
     *        - commentList : 게시물 comment list
     * @return
     */
    @GetMapping("/article/{articleId}")
    public String articleView(
            @PathVariable Long articleId,
            @CookieValue(required = false, name = "hit") String hitCookieValue,
            Model model,
            HttpServletResponse response
    ) {

        boolean hitCheck = checkDuplicateHitCount(articleId, hitCookieValue, response);

        List<CommentListResponse> commentList = commentService.findCommentList(articleId);
        ArticleDetailResponse articleDetailResponse = articleService.findArticleDetail(articleId, hitCheck);
        model.addAttribute("articleDetailResponse", articleDetailResponse);
        model.addAttribute("commentList", commentList);

        return "article/articleView";
    }


    /**
     * 게시물 카테고리별 조회
     * @param pageable : 페이지 정보
     * @param categoryTitle : category title
     * @return
     */
    @GetMapping("/article")
    public String findArticleByCategory(
            @PageableDefault(size = 8, sort = "id",direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam String categoryTitle,
            Model model
    ) {
        Page<ArticleCardBoxResponse> articleAll = articleService.findArticleByCategory(categoryTitle, pageable);
        model.addAttribute("articleCardBoxList", articleAll);
        model.addAttribute("pageDto", PageDto.of(articleAll));

        return "article/articleList";
    }

    /**
     * 검색어로 게시물 조회
     * @param pageable
     * @param keyword
     * @return
     */
    @GetMapping("/article-search")
    public String findSearchArticleByKeyword(
            @PageableDefault(size = 8, sort = "id",direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam String keyword,
            Model model
    ) {
        Page<ArticleCardBoxResponse> articleByKeywordResponse = articleService.findSearchArticle(keyword, pageable);
        model.addAttribute("articleCardBoxList", articleByKeywordResponse);
        model.addAttribute("pageDto", PageDto.of(articleByKeywordResponse));
        return "article/articleList";
    }

    @GetMapping("/article-tag")
    public String findArticleByTag(
            @PageableDefault(size = 8, sort = "id",direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam String tag,
            Model model
    ) {
        Page<ArticleCardBoxResponse> articleByTagResponse = articleService.findArticleByTag(tag, pageable);
        model.addAttribute("articleCardBoxList", articleByTagResponse);
        model.addAttribute("pageDto", PageDto.of(articleByTagResponse));
        System.out.println("PageDto.of(articleByTagResponse) = " + PageDto.of(articleByTagResponse));
        return "article/articleList";
    }

    @GetMapping("/admin/article")
    public String adminSearchArticle(
            @PageableDefault(size = 8, sort = "id",direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam String categoryTitle,
            @AuthenticationPrincipal CustomOauth2User customOauth2User,
            Model model
    ) {
        RightLoginChecker.checkAdminMember(customOauth2User);
        Page<ArticleCardBoxResponse> articleAll = articleService.findArticleByCategory(categoryTitle, pageable);
        model.addAttribute("articleCardBoxList", articleAll);
        model.addAttribute("pageDto", PageDto.of(articleAll));
        return "admin/article/adminArticleList";
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
