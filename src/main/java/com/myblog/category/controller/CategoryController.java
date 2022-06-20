package com.myblog.category.controller;

import com.myblog.category.dto.CategoryListResponse;
import com.myblog.category.model.Category;
import com.myblog.category.resposiotry.CategoryRepository;
import com.myblog.category.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    private final CategoryRepository categoryRepository;


    @GetMapping("/admin/categories")
    public String categoryList(Model model) {

        categoryRepository.save(new Category("category1"));
        CategoryListResponse categoryListResponse = categoryService.findCategories();
        model.addAttribute("categoryListResponse", categoryListResponse);

        return "admin/category/categoryList";
    }


    @PostMapping("/admin/categories")
    public @ResponseBody String editCategory(@RequestBody CategoryListResponse categoryListResponse) {
        categoryService.editCategory(categoryListResponse);
        return "성공";
    }
}
