package com.myblog.category.dto;

import com.myblog.category.model.Category;
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
    private int count;

    public static ChildCategoryList of(Category category) {
        return new ChildCategoryList(
                category.getId(),
                category.getTitle(),
                false,
                0
        );
    }

    public boolean checkNewCategory(){
        return id == null;
    }

    public void setCount(int count) {
        this.count = count;
    }

}
