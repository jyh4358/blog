package com.myblog.article.dto;

import lombok.*;

@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ArticleWriteDto {

    private String title;
    private String content;

//    private String thumbnailUrl;
    private String category;
    private String tags;

}
