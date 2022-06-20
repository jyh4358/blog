package com.myblog.category.service;

import com.myblog.category.dto.CategoryList;
import com.myblog.category.dto.CategoryListResponse;
import com.myblog.category.dto.ChildCategoryList;
import com.myblog.category.model.Category;
import com.myblog.category.resposiotry.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {

    private final CategoryRepository categoryRepository;


    public CategoryListResponse findCategories() {
        List<Category> categories = categoryRepository.findAll();
        List<CategoryList> categoryLists = categories.stream().map(
                s -> new CategoryList(s.getId(),
                        s.getTitle(),
                        s.getChild().stream().map(
                                t -> new ChildCategoryList(
                                        t.getId(),
                                        t.getTitle(),
                                        false)).collect(Collectors.toList()),
                        false)).collect(Collectors.toList());
        return CategoryListResponse.of(categoryLists);
    }

    @Transactional
    public void editCategory(CategoryListResponse categoryListResponse) {

        List<Category> categories = categoryRepository.findAll();
        List<CategoryList> categoryLists = categoryListResponse.getCategoryLists();

        for (CategoryList categoryList : categoryLists) {
            changeCategoryTitle(categoryList, categories);
            createNewCategory(categoryList, categories);
        }
    }

    public void changeCategoryTitle(CategoryList categoryList, List<Category> categories) {
        categories.forEach( s ->
        {
            if (s.getId() == categoryList.getId()) {
                s.changeTitle(categoryList.getParentCategory());
                List<ChildCategoryList> collect =
                        categoryList.getChildCategories().stream().filter(f -> !(f.isNewCategory()))
                                .collect(Collectors.toList());

                List<Category> children = collect.stream().map(t ->
                        Category.createCategory(t.getId(), t.getChildCategory(), s)).collect(Collectors.toList()
                );
                s.setChild(children);
            }
        });
    }

    public void createNewCategory(CategoryList categoryList, List<Category> categories) {
        if (categoryList.isNewCategory()) {
            categoryRepository.save(Category.createCategory(null, categoryList.getParentCategory(), null));
        }
        for (Category category : categories) {
            if (categoryList.getId() == category.getId()) {
                List<ChildCategoryList> collect = categoryList.getChildCategories().stream().filter(f -> f.isNewCategory())
                        .collect(Collectors.toList());
                collect.stream().forEach(s ->
                        categoryRepository.save(Category.createCategory(null, s.getChildCategory(), category))
                );

            }
        }
    }

}
