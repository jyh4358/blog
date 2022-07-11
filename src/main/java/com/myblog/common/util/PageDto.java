package com.myblog.common.util;

import lombok.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Getter
@Setter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PageDto {

    private int currentPageNum;
    private int lastPageNum;

    private int startPageNum;
    private int endPageNum;

}
