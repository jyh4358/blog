package com.myblog.category.controller;

import com.myblog.category.dto.CategoryListResponse;
import com.myblog.category.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;


    @GetMapping("/admin/categories")
    public String categoryList(Model model) {

        CategoryListResponse categoryListResponse = categoryService.findCategories();

        model.addAttribute("categoryListResponse", categoryListResponse);

        return "admin/category/categoryList";
    }

    @PostMapping("/admin/categoryes")
    public String category
}
