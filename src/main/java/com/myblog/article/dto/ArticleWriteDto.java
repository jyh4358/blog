package com.myblog.article.dto;

import lombok.*;

@Getter @Setter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ArticleWriteDto {

    private String title;
    private String content;
    private String thumbnailUrl;
    private Long category;
    private String tags;



    public static ArticleWriteDto createDefaultArticleDto() {
        return new ArticleWriteDto();
    }

}
