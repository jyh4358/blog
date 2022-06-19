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
public class CategoryListResponse {

    private List<CategoryList> categoryLists = new ArrayList<>();

    public static CategoryListResponse of(List<CategoryList> categoryLists) {
        return new CategoryListResponse(categoryLists);
    }
}
