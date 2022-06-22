package com.myblog.article.dto;

import lombok.*;

@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@AllArgsConstructor
public class ArticleWriteDto {

    private String title;
    private String content;
    private String thumbnailUrl;
    private Long category;
    private String tags;



    public static ArticleWriteDto createDefaultArticleWriteDto() {
        return new ArticleWriteDto();
    }

}
