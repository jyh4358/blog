package com.myblog.article.dto;

import com.myblog.article.model.Article;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class SimpleArticle {
    private Long id;
    private String title;

    public static SimpleArticle of(Article article) {
        return new SimpleArticle(
                article.getId(),
                article.getTitle()
        );
    }
}
