package com.myblog.category.dto;

import lombok.*;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CategoryQueryDto {
    private Long id;
    private Long count;
}
