package com.myblog.article.dto;

import com.myblog.article.model.Tag;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TagResponse {
    private String name;

    public TagResponse(String name) {
        this.name = name;
    }

    public static TagResponse of(String name) {
        return new TagResponse(name);
    }
}
