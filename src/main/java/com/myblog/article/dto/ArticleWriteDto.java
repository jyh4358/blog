package com.myblog.article.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter @Setter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ArticleWriteDto {

    @NotBlank(message = "제목을 입력해주세요.")
    private String title;
    @NotBlank(message = "내용을 입력해주세요.")
    private String content;
    private String thumbnailUrl;
    @NotNull(message ="카테고리를 선택해주세요")
    private Long category;
    @NotBlank(message = "태그를 입력해주세요.")
    private String tags;

}
