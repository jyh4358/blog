package com.myblog.category.controller;

import com.myblog.category.dto.ParentCategoryList;
import com.myblog.category.dto.CategoryListDto;
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

        CategoryListDto categoryListDto = categoryService.findCategories();
        model.addAttribute("categoryListDto", categoryListDto);

        return "admin/category/categoryList";
    }


    @PostMapping("/admin/categories")
    public @ResponseBody String editCategory(@RequestBody CategoryListDto categoryListDto) {
        categoryService.editCategory(categoryListDto);
        return "성공";
    }
}
