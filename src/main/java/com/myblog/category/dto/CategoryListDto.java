package com.myblog.category.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CategoryListDto {

    private List<ParentCategoryList> parentCategoryLists = new ArrayList<>();
    private int totalCount;

    public static CategoryListDto of(List<ParentCategoryList> parentCategoryLists) {
        return new CategoryListDto(parentCategoryLists, 0);
    }

    public void addTotalCount(int count) {
        this.totalCount += count;
    }
}
