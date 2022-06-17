package com.myblog;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class TestList2 {
    private Long num;
    private String child;

    public TestList2() {
    }

    public TestList2(Long num, String child) {
        this.num = num;
        this.child = child;

    }
}
