package com.myblog.category.dto;

import com.myblog.category.model.Category;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ParentCategoryList {
    private Long id;
    private String parentCategory;
    private List<ChildCategoryList> childCategoryLists = new ArrayList<>();
    private Boolean deleteCheck;
    private int count;


    public static ParentCategoryList of(Category category) {
        return new ParentCategoryList(
                category.getId(),
                category.getTitle(),
                category.getChild().stream().map(
                        t -> ChildCategoryList.of(t)
                ).collect(Collectors.toList()),
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

    public void addParentCount(int count) {
        this.count += count;
    }
}
