package com.myblog;

import com.myblog.article.dto.ArticleWriteDto;
import com.myblog.article.model.Article;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiTestController {

    @GetMapping("/login/test")
    public ResponseEntity<ArticleWriteDto> test(@RequestParam String code) {

        System.out.println("code = " + code);

        ArticleWriteDto articleWriteDto = new ArticleWriteDto(
                "title",
                "content",
                "category",
                "tags"
        );


        return new ResponseEntity<>(articleWriteDto, HttpStatus.OK);
    }

}
