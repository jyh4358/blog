package com.myblog.common.util;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class PagingUtill {

    private static final int DISPLAY_PAGE_SIZE = 5;

    public static PageDto createPageDto(Page pageableDto) {
        Pageable articlePageable = pageableDto.getPageable();

        if (pageableDto.getTotalPages() == 0) {
            return new PageDto(0, 0, 0, 0);
        }

        return new PageDto(
                articlePageable.getPageNumber(),
                pageableDto.getTotalPages() - 1,
                Math.min((int) Math.floor(articlePageable.getPageNumber() / DISPLAY_PAGE_SIZE) * DISPLAY_PAGE_SIZE, pageableDto.getTotalPages()- 1),
                Math.min(pageableDto.getTotalPages() - 1, (int) (Math.ceil(articlePageable.getPageNumber() / DISPLAY_PAGE_SIZE + 1) * DISPLAY_PAGE_SIZE - 1))
        );
    }

}
