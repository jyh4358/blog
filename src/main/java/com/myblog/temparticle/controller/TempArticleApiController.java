package com.myblog.temparticle.controller;

import com.myblog.security.oauth2.model.CustomOauth2User;
import com.myblog.temparticle.dto.TempArticleDto;
import com.myblog.temparticle.service.TempArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class TempArticleApiController {

    private final TempArticleService tempArticleService;

    @GetMapping("/api/v1/admin/article-auto")
    public ResponseEntity<TempArticleDto> findAutoSavedArticle(
            @AuthenticationPrincipal CustomOauth2User customOauth2User
    ) {

        TempArticleDto autoSavedArticle = tempArticleService.findAutoSavedArticle(customOauth2User);

        return new ResponseEntity<>(autoSavedArticle, HttpStatus.OK);
    }

    @PostMapping("/api/v1/admin/article-auto")
    public ResponseEntity<Void> autoSaveArticle(
            @RequestBody TempArticleDto tempArticleDto,
            @AuthenticationPrincipal CustomOauth2User customOauth2User
    ) {

        tempArticleService.saveTempArticle(tempArticleDto, customOauth2User);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/api/v1/admin/article-auto/{tempArticleId}")
    public ResponseEntity<Void> deleteAutoSavedArticle(
            @PathVariable Long tempArticleId,
            @AuthenticationPrincipal CustomOauth2User customOauth2User
    ) {
        tempArticleService.deleteAutoSavedArticle(tempArticleId, customOauth2User);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
