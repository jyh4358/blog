package com.myblog;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class CategoryList {
    private List<TestList> testLists = new ArrayList<>();

    public CategoryList() {
    }

    public CategoryList(List<TestList> testLists) {
        this.testLists = testLists;
    }
    
}
