package com.myblog.article.dto;

import lombok.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Getter
@Setter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PageDto {

    private static final int DISPLAY_PAGE_SIZE = 5;

    private int currentPageNum;
    private int lastPageNum;

    private int startPageNum;
    private int endPageNum;


    public static PageDto of(Page pageableDto) {
        Pageable articlePageable = pageableDto.getPageable();

        if (pageableDto.getTotalPages() == 0) {
            return new PageDto(0, 0, 0, 0);
        }

        return new PageDto(
                articlePageable.getPageNumber(),
                pageableDto.getTotalPages() - 1,
                (int) Math.floor(articlePageable.getPageNumber() / DISPLAY_PAGE_SIZE) * DISPLAY_PAGE_SIZE,
                Math.min(pageableDto.getTotalPages() - 1, (int)(Math.ceil(articlePageable.getPageNumber() / DISPLAY_PAGE_SIZE + 1) * DISPLAY_PAGE_SIZE - 1))
        );
    }
}
