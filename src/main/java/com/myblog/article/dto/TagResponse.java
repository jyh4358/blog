package com.myblog.article.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
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
