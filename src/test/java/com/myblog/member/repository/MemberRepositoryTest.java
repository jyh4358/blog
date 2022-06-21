package com.myblog.member.repository;

import com.myblog.category.model.Category;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;

class MemberRepositoryTest {

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

}