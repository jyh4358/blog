package com.myblog.member.repository;

import com.myblog.category.model.Category;
import com.myblog.category.resposiotry.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;

class MemberRepositoryTest {

    @Autowired
    CategoryRepository categoryRepository;

    @Test
    public void 날짜() {

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime now1 = LocalDateTime.now();
        LocalDateTime now2 = now1.plusDays(10L);
        ChronoUnit.DAYS.between(now, now2);

        System.out.println("now = " + now);
        System.out.println("now1 = " + now2);

        System.out.println("ChronoUnit.DAYS.between(now, now1) = " + ChronoUnit.DAYS.between(now, now2));

        Category title = new Category("title", null);
        System.out.println("title.getId() = " + title.getId());
    }

    @Test
    public void 테스트() {
        String[] test= {"12", "22", "32"};
        boolean b = Arrays.stream(test).anyMatch(s -> s.equals("33"));
        System.out.println("b = " + b);
    }

}