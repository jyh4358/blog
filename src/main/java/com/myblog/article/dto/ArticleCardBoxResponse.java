package com.myblog.article.dto;

import com.myblog.article.model.Article;
import lombok.*;
import org.jsoup.Jsoup;

import java.time.LocalDateTime;

import static com.myblog.common.util.MarkdownUtils.getHtmlRenderer;
import static com.myblog.common.util.MarkdownUtils.getParser;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ArticleCardBoxResponse {
    private Long id;
    private String title;
    private String content;
    private String thumbnailUrl;
    private LocalDateTime createDate;

    public static ArticleCardBoxResponse of(Article article) {
        ArticleCardBoxResponse articleCardBoxResponse = new ArticleCardBoxResponse(
                article.getId(),
                article.getTitle(),
                article.getContent(),
                article.getThumbnailUrl(),
                article.getCreatedDate()
        );
        articleCardBoxResponse.simpleContentConvert();
        return articleCardBoxResponse;
    }

    public void simpleContentConvert() {
        if (this.content.length() > 300) {
            this.content = this.content.substring(0, 300);
        }
        this.content = Jsoup.parse(getHtmlRenderer().render(getParser().parse(this.content))).text();

    }
}
