package com.myblog.article.controller;

import com.myblog.article.dto.ArticleCardBoxResponse;
import com.myblog.article.dto.ArticleWriteDto;
import com.myblog.article.dto.PopularArticleResponse;
import com.myblog.article.model.Article;
import com.myblog.article.service.ArticleService;
import com.myblog.article.service.GitHubBackupService;
import com.myblog.security.oauth2.model.CustomOauth2User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ArticleApiController {

    private final ArticleService articleService;
    private final GitHubBackupService gitHubBackupService;


    /**
     * index 페이지 최신게시물 무한스크롤
     * @param curPage
     * @return
     */
    @GetMapping("/api/v1/article")
    public ResponseEntity<List<ArticleCardBoxResponse>> infinityScroll(@RequestParam int curPage) {

        List<ArticleCardBoxResponse> popularArticleResponse = articleService.findRecentArticle(curPage);

        return new ResponseEntity<>(popularArticleResponse, HttpStatus.OK);
    }

    /**
     * 게시물 작성
     * @param articleWriteDto
     * @param customOauth2User
     * @return
     */
    @PostMapping("/api/v1/admin/article")
    public ResponseEntity<Long> saveArticle(
            @RequestBody @Valid ArticleWriteDto articleWriteDto,
            @AuthenticationPrincipal CustomOauth2User customOauth2User
    ) {
        System.out.println("articleWriteDto = " + articleWriteDto);

        Article savedArticle = articleService.writeArticle(articleWriteDto, customOauth2User);
        gitHubBackupService.backupArticleToGitHub(savedArticle);
        return new ResponseEntity<>(savedArticle.getId(), HttpStatus.OK);
    }

    /**
     * 게시물 삭제
     * @param articleId
     * @param customOauth2User
     * @return
     */
    @DeleteMapping("/api/v1/admin/article/{articleId}")
    public ResponseEntity<Void> deleteArticle(
            @PathVariable Long articleId,
            @AuthenticationPrincipal CustomOauth2User customOauth2User
    ) {
        articleService.deleteArticle(articleId, customOauth2User);

        return new ResponseEntity<>(HttpStatus.OK);
    }


    /**
     * 게시물 수정
     * @param articleId
     * @param articleWriteDto
     * @param customOauth2User
     * @return
     */
    @PatchMapping("/api/v1/admin/article/{articleId}")
    public ResponseEntity<Void> modifyArticle(
            @PathVariable Long articleId,
            @RequestBody @Valid ArticleWriteDto articleWriteDto,
            @AuthenticationPrincipal CustomOauth2User customOauth2User
    ) {
        articleService.modifyArticle(articleId, articleWriteDto, customOauth2User);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
