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
public class ParentCategoryList {
    private Long id;
    private String parentCategory;
    private List<ChildCategoryList> childCategoryLists = new ArrayList<>();
    private Boolean deleteCheck;

    public boolean checkNewCategory(){
        return id == null;
    }
}
