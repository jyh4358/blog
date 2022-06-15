package com.myblog;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TestList {
    private Long num;
    private String parentCategory;
    private List<String> child;


    public TestList(Long num, String parentCategory, List<String> child) {
        this.num = num;
        this.parentCategory = parentCategory;
        this.child = child;
    }
}
