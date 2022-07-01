package com.myblog.temparticle.controller;

import com.myblog.temparticle.dto.TempArticleDto;
import com.myblog.temparticle.service.TempArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class TempArticleApiController {

    private final TempArticleService tempArticleService;

    @GetMapping("/api/v1/admin/article-auto")
    public ResponseEntity<TempArticleDto> findAutoSavedArticle() {

        TempArticleDto autoSavedArticle = tempArticleService.findAutoSavedArticle();

        TempArticleDto tempArticleDto = new TempArticleDto();

        return new ResponseEntity<>(autoSavedArticle, HttpStatus.OK);
    }

    @PostMapping("/api/v1/admin/article-auto")
    public ResponseEntity<Void> autoSaveArticle(
            @RequestBody TempArticleDto tempArticleDto
    ) {
        System.out.println("tempArticleDto = " + tempArticleDto);

        tempArticleService.saveTempArticle(tempArticleDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/api/v1/admin/article-auto/{tempArticleId}")
    public ResponseEntity<Void> deleteAutoSavedArticle(
            @PathVariable Long tempArticleId
    ) {
        tempArticleService.deleteAutoSavedArticle(tempArticleId);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
