package com.myblog.category.controller;

import com.myblog.category.dto.CategoryListDto;
import com.myblog.category.resposiotry.CategoryRepository;
import com.myblog.category.service.CategoryService;
import com.myblog.security.oauth2.model.CustomOauth2User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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


    @GetMapping("/admin/categories")
    public String categoryList(Model model) {

        CategoryListDto categoryListDto = categoryService.findCategories();
        model.addAttribute("categoryListDto", categoryListDto);

        return "admin/category/categoryList";
    }


    @PostMapping("/admin/categories")
    public @ResponseBody ResponseEntity<Void> editCategory(
            @RequestBody CategoryListDto categoryListDto,
            @AuthenticationPrincipal CustomOauth2User customOauth2User
    ) {
        categoryService.editCategory(categoryListDto, customOauth2User);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
