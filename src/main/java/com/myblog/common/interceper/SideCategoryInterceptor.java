package com.myblog.common.interceper;

import com.myblog.category.dto.CategoryListDto;
import com.myblog.category.service.CategoryService;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SideCategoryInterceptor implements HandlerInterceptor {

    private CategoryService categoryService;

    public SideCategoryInterceptor(CategoryService categoryService) {
        this.categoryService = categoryService;
    }


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        System.out.println("preHandle===================== ");
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        CategoryListDto sideBarCategoryListDto = categoryService.findSidebarCategory();
//        System.out.println("sideBarCategoryListDto = " + sideBarCategoryListDto);
        modelAndView.addObject("sideBarCategoryListDto", sideBarCategoryListDto);
        modelAndView.addObject("testObject", "test");
    }


}
