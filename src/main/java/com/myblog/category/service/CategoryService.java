package com.myblog.category.service;

import com.myblog.category.dto.CategoryList;
import com.myblog.category.dto.CategoryListResponse;
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
        List<CategoryList> categoryLists = categories.stream().map(s ->
                new CategoryList(s.getTitle(), s.getChild().stream().map(t ->
                        t.getTitle()).collect(Collectors.toList()))).collect(Collectors.toList());
        return CategoryListResponse.of(categoryLists);
    }
}
