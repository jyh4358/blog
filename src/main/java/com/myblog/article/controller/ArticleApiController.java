package com.myblog.article.controller;

import com.myblog.article.dto.ArticleWriteDto;
import com.myblog.article.service.ArticleService;
import com.myblog.security.oauth2.CustomOauth2User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ArticleApiController {

    private final ArticleService articleService;

    @DeleteMapping("/api/v1/admin/article-delete/{articleId}")
    public ResponseEntity<Void> deleteArticle(
            @PathVariable Long articleId
    ) {
        articleService.deleteArticle(articleId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/api/v1/admin/article-save")
    public ResponseEntity<Void> saveArticle(
            @RequestBody ArticleWriteDto articleWriteDto,
            @AuthenticationPrincipal CustomOauth2User customOauth2User
    ) {
        System.out.println("articleWriteDto = " + articleWriteDto);

        articleService.writeArticle(articleWriteDto, customOauth2User);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @PatchMapping("/api/v1/admin/article-modify/{articleId}")
    public ResponseEntity<Void> modifyArticle(
            @PathVariable Long articleId,
            @RequestBody ArticleWriteDto articleWriteDto,
            @AuthenticationPrincipal CustomOauth2User customOauth2User
    ) {

        articleService.modifyArticle(articleId, articleWriteDto);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
