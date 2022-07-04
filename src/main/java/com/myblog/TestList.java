package com.myblog;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class TestList {
    private String parentCategory;
    private List<String> childCategories = new ArrayList<>();

    public TestList() {
    }

    public TestList(String parentCategory, List<String> childCategories) {
        this.parentCategory = parentCategory;
        this.childCategories = childCategories;
    }
}
