package com.myblog.category.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ChildCategoryList {
    private Long id;
    private String childCategory;
    private Boolean deleteCheck;

    public boolean checkNewCategory(){
        return id == null;
    }

}
