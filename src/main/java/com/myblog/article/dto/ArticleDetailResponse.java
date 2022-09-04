package com.myblog.article.dto;

import com.myblog.article.model.Article;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.myblog.common.util.MarkdownUtils.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ArticleDetailResponse {
    private Long id;
    private String title;
    private String content;
    private long hit;
    private String username;
    private String thumbnailUrl;
    private String category;
    private List<String> tags = new ArrayList<>();
    private LocalDateTime createDate;
    private List<ArticleSimpleDto> articleSimpleDtos = new ArrayList<>();

    @Builder
    public ArticleDetailResponse(Long id,
                                 String title,
                                 String content,
                                 long hit,
                                 String username,
                                 String thumbnailUrl,
                                 String category,
                                 List<String> tags,
                                 LocalDateTime createDate) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.hit = hit;
        this.username = username;
        this.thumbnailUrl = thumbnailUrl;
        this.category = category;
        this.tags = tags;
        this.createDate = createDate;
    }

    public static ArticleDetailResponse of(
            Article article,
            List<String> tagList,
            String username) {
        return ArticleDetailResponse.builder()
                .id(article.getId())
                .title(article.getTitle())
                .content(getHtmlRenderer().render(getParser().parse(article.getContent())))
                .hit(article.getHit())
                .username(username)
                .thumbnailUrl(article.getThumbnailUrl())
                .category(article.getCategory().getTitle())
                .tags(tagList)
                .createDate(article.getCreatedDate())
                .build();


    }

}
