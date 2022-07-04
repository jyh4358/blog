package com.myblog.article.dto;

import com.myblog.article.model.Article;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ArticleModifyResponse {
    private Long id;
    private String title;
    private String content;
    private String thumbnailUrl;
    private List<String> tags;
    private String category;

    public ArticleModifyResponse(Long id, String title, String content, String thumbnailUrl, List<String> tags, String category) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.thumbnailUrl = thumbnailUrl;
        this.tags = tags;
        this.category = category;
    }
    public static ArticleModifyResponse of(Article article, List<String> tags) {
        return new ArticleModifyResponse(
                article.getId(),
                article.getTitle(),
                article.getContent(),
                article.getThumbnailUrl(),
                tags,
                article.getCategory().getTitle()
        );
    }
}
