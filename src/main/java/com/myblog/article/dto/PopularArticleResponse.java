package com.myblog.article.dto;

import com.myblog.article.model.Article;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PopularArticleResponse {
    private Long id;
    private String title;
    private String thumbnailUrl;
    private LocalDateTime createDate;

    public PopularArticleResponse(Long id, String title, String thumbnailUrl, LocalDateTime createDate) {
        this.id = id;
        this.title = title;
        this.thumbnailUrl = thumbnailUrl;
        this.createDate = createDate;
    }

    public static PopularArticleResponse of(Article article) {
        return new PopularArticleResponse(
                article.getId(),
                article.getTitle(),
                article.getThumbnailUrl(),
                article.getCreatedDate()
        );
    }
}
