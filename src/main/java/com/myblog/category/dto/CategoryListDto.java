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

    public static CategoryListDto of(List<ParentCategoryList> parentCategoryLists) {
        return new CategoryListDto(parentCategoryLists);
    }
}
