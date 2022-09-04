package com.myblog.article.dto;

import com.myblog.article.model.Article;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ArticleSimpleDto {
    private Long id;
    private String title;

    public static ArticleSimpleDto of(Article article) {
        return new ArticleSimpleDto(
                article.getId(),
                article.getTitle()
        );
    }
}
