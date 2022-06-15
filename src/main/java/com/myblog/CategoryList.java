package com.myblog;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CategoryList {
    List<TestList> list;


    public CategoryList(List<TestList> list) {
        this.list = list;
    }
}
