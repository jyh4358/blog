package com.myblog.category.service;

import com.myblog.category.dto.CategoryListDto;
import com.myblog.category.dto.CategoryQueryDto;
import com.myblog.category.dto.ChildCategoryList;
import com.myblog.category.dto.ParentCategoryList;
import com.myblog.category.model.Category;
import com.myblog.category.resposiotry.CategoryRepository;
import com.myblog.common.checker.RightLoginChecker;
import com.myblog.security.oauth2.model.CustomOauth2User;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {

    private final CategoryRepository categoryRepository;

    /*
        - 카테고리 조회
     */
    public CategoryListDto findCategories() {
        List<ParentCategoryList> parentCategoryLists = categoryRepository.findAll().stream()
                .filter(parentCategory -> parentCategory.getParent() == null)
                .map(parentCategory -> ParentCategoryList.of(parentCategory))
                .collect(Collectors.toList());

        return CategoryListDto.of(parentCategoryLists);
    }

    /*
        - 카테고리 등록 및 수정
     */
    @Transactional
    @CacheEvict(value = "sideBarCategoryCaching", allEntries = true)
    public void editCategory(CategoryListDto categoryListDto, CustomOauth2User customOauth2User) {
        RightLoginChecker.checkAdminMember(customOauth2User);

        List<Category> categories = categoryRepository.findAll();
        List<ParentCategoryList> parentCategoryLists = categoryListDto.getParentCategoryLists();

        for (ParentCategoryList parentCategoryList : parentCategoryLists) {
            modifyCategoryTitle(parentCategoryList, categories);
            createNewCategory(parentCategoryList, categories);
            deleteCategory(parentCategoryList, categories);

        }
    }

    /*
        사이드바 카테고리와 카테고리에 있는 게시물 수 조회
     */
    @Cacheable(value = "sideBarCategoryCaching", key = "0")
    public CategoryListDto findSidebarCategory() {
        CategoryListDto categoryListDto = findCategories();
        List<CategoryQueryDto> categoryCounts = categoryRepository.findCategoryCount();

        for (CategoryQueryDto categoryCount : categoryCounts) {
            categoryListDto.getParentCategoryLists()
                    .forEach(
                            parentCategory -> {
                                if (parentCategory.getId().equals(categoryCount.getId())) {
                                    parentCategory.setCount((categoryCount.getCount().intValue()));
                                } else {
                                    insertChildCategoryCount(parentCategory, categoryCount);
                                }
                            }
                    );
        }

        parentCategoryTotalCalc(categoryListDto);
        categoryTotalCalc(categoryListDto);


        return categoryListDto;
    }



    // 서비스 로직
    private void insertChildCategoryCount(ParentCategoryList parentCategoryList, CategoryQueryDto categoryCount) {
        for (ChildCategoryList childCategoryList : parentCategoryList.getChildCategoryLists()) {
            if (childCategoryList.getId().equals(categoryCount.getId())) {
                childCategoryList.setCount(categoryCount.getCount().intValue());
            }
        }
    }

    private void modifyCategoryTitle(ParentCategoryList parentCategoryList, List<Category> categories) {
        categories.forEach(category ->
        {
            if (category.getId() == parentCategoryList.getId()) {
                category.changeTitle(parentCategoryList.getParentCategory());

                parentCategoryList.getChildCategoryLists().stream()
                        .filter(childCategory -> !(childCategory.checkNewCategory()))
                        .forEach(childCategory -> modifyChildCategoryTitle(childCategory, category.getChild()));
            }
        });
    }

    private void modifyChildCategoryTitle(ChildCategoryList childCategoryList, List<Category> categories) {
        for (Category category : categories) {
            if (category.getId().equals(childCategoryList.getId())) {
                category.changeTitle(childCategoryList.getChildCategory());
            }
        }
    }

    private void createNewCategory(ParentCategoryList parentCategoryList, List<Category> categories) {
        if (parentCategoryList.checkNewCategory()) {
            categoryRepository.save(new Category(parentCategoryList.getParentCategory()));
        }
        for (Category category : categories) {
            if (parentCategoryList.getId() == category.getId()) {

                parentCategoryList.getChildCategoryLists().stream()
                        .filter(childCategory -> childCategory.checkNewCategory())
                        .forEach(childCategory ->
                        categoryRepository.save(Category.createCategory(null, childCategory.getChildCategory(), category))
                );
            }
        }
    }

    private void deleteCategory(ParentCategoryList parentCategoryList, List<Category> categories) {
        if (parentCategoryList.getDeleteCheck()) {
            categories.forEach(category ->
            {
                if (category.getId().equals(parentCategoryList.getId())) {
                    categoryRepository.delete(category);
                }
            });
        } else {
            List<ChildCategoryList> deleteChildCategories = parentCategoryList.getChildCategoryLists().stream()
                    .filter(childCategory -> childCategory.getDeleteCheck())
                    .collect(Collectors.toList());

            categories.forEach(childCategory ->
            {
                if (childCategory.getId().equals(parentCategoryList.getId())) {
                    deleteChildCategories.forEach(deleteChildCategory ->
                            deleteChildCategoryTitle(deleteChildCategory, childCategory.getChild()));
                }
            });

        }

    }

    private void deleteChildCategoryTitle(ChildCategoryList childCategoryList, List<Category> child) {
        for (Iterator<Category> it = child.iterator() ; it.hasNext() ;) {
            Category category = it.next();
            if (category.getId().equals(childCategoryList.getId())) {
                it.remove();
                categoryRepository.delete(category);
            }
        }

    }

    private void parentCategoryTotalCalc(CategoryListDto categoryListDto) {
        for (ParentCategoryList parentCategoryList : categoryListDto.getParentCategoryLists()) {
            parentCategoryList.getChildCategoryLists()
                    .forEach(childCategory -> parentCategoryList.addParentCount(childCategory.getCount()));
        }
    }

    private void categoryTotalCalc(CategoryListDto categoryListDto) {
        for (ParentCategoryList parentCategoryList : categoryListDto.getParentCategoryLists()) {
            categoryListDto.addTotalCount(parentCategoryList.getCount());
        }
    }


}
