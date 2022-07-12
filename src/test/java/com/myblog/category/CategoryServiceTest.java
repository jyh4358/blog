package com.myblog.category;

import com.myblog.category.dto.CategoryListDto;
import com.myblog.category.dto.ParentCategoryList;
import com.myblog.category.model.Category;
import com.myblog.category.service.CategoryService;
import com.myblog.commen.TestHelper;
import com.myblog.commen.basetest.ServiceBaseTest;
import com.myblog.member.model.Member;
import com.myblog.security.oauth2.model.CustomOauth2User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

public class CategoryServiceTest extends ServiceBaseTest {
    @Autowired
    private CategoryService categoryService;

    @Test
    @Transactional
    @DisplayName("카테고리 생성")
    public void 카테고리_생성() {
        // given
        Member member = TestHelper.관리자_생성(memberRepository);
        CustomOauth2User customOauth2User = TestHelper.인증_객체_생성(member);

        ParentCategoryList categoryDto = new ParentCategoryList(null, "category1", new ArrayList<>(), false, 0);
        CategoryListDto categoryListDto = new CategoryListDto(List.of(categoryDto), 0);

        // when
        categoryService.editCategory(categoryListDto, customOauth2User);

        // then
        Category category = categoryRepository.findAll().get(0);
        Assertions.assertThat(category.getTitle()).isEqualTo(categoryListDto.getParentCategoryLists().get(0).getParentCategory());
    }

    @Test
    @Transactional
    @DisplayName("카테고리 조회")
    public void 카테고리_조회() {
        // given
        Category savedCategory = categoryRepository.save(new Category("category"));
        // when
        CategoryListDto findCategoryList = categoryService.findCategories();

        // then
        Assertions.assertThat(findCategoryList.getParentCategoryLists().get(0).getId()).isEqualTo(savedCategory.getId());
        Assertions.assertThat(findCategoryList.getParentCategoryLists().get(0).getParentCategory()).isEqualTo(savedCategory.getTitle());
    }

    @Test
    @Transactional
    @DisplayName("카테고리 수정")
    public void 카테고리_수정() {
        //given
        Member member = TestHelper.관리자_생성(memberRepository);
        CustomOauth2User customOauth2User = TestHelper.인증_객체_생성(member);
        Category savedCategory = categoryRepository.save(new Category("category"));

        ParentCategoryList categoryDto = new ParentCategoryList(savedCategory.getId(), "category edit", new ArrayList<>(), false, 0);
        CategoryListDto categoryListDto = new CategoryListDto(List.of(categoryDto), 0);

        // when
        categoryService.editCategory(categoryListDto, customOauth2User);

        // then
        Category editCategory = categoryRepository.findAll().get(0);

        Assertions.assertThat(editCategory.getTitle()).isEqualTo(categoryListDto.getParentCategoryLists().get(0).getParentCategory());

    }
}
