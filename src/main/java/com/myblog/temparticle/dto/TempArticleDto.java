package com.myblog.temparticle.dto;

import com.myblog.temparticle.model.TempArticle;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class TempArticleDto {
    private String title;
    private String content;
    private String category;
    private String thumbnailUrl;
    private String tag;


    public static TempArticleDto of(TempArticle tempArticle) {
        return new TempArticleDto(
                tempArticle.getTitle(),
                tempArticle.getContent(),
                tempArticle.getCategory(),
                tempArticle.getThumbnailUrl(),
                tempArticle.getTags()
        );
    }

}
