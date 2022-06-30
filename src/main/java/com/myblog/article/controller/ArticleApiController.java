package com.myblog.article.controller;

import com.myblog.article.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

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
}
