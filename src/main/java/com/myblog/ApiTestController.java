package com.myblog;

import com.myblog.article.dto.ArticleWriteDto;
import com.myblog.article.dto.PopularArticleResponse;
import com.myblog.article.model.Article;
import com.myblog.article.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class ApiTestController {
    
    private final ArticleService articleService;

    //    @GetMapping("/login/test")
    public ResponseEntity<ArticleWriteDto> test(@RequestParam String code) {

        System.out.println("code = " + code);


        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/google/login")
    public String test2() {
        String redirectUri = UriComponentsBuilder
                .fromUriString("https://accounts.google.com/o/oauth2/v2/auth")
                .queryParam("client_id", "428541390243-7cevccqe0afejrec8et1025hbk8v36p0.apps.googleusercontent.com")
                .queryParam("response_type", "code")
                .queryParam("scope", "email profile")
                .queryParam("redirect_uri", "http://localhost:8080/login/test")
                .toUriString();


        return redirectUri;
    }

    @GetMapping("/api/v1/article")
    public ResponseEntity<List<PopularArticleResponse>> index(@RequestParam int curPage) {

        List<PopularArticleResponse> popularArticleResponse = articleService.findRecentArticle(curPage);

        return new ResponseEntity<>(popularArticleResponse, HttpStatus.OK);
    }
}
