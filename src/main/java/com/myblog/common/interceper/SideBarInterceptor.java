package com.myblog.common.interceper;

import com.myblog.category.dto.CategoryListDto;
import com.myblog.category.service.CategoryService;
import com.myblog.comment.dto.RecentCommentResponse;
import com.myblog.comment.service.CommentService;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class SideBarInterceptor implements HandlerInterceptor {

    private CategoryService categoryService;
    private CommentService commentService;

    public SideBarInterceptor(CategoryService categoryService, CommentService commentService) {
        this.categoryService = categoryService;
        this.commentService = commentService;
    }


    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if (modelAndView != null) {
            CategoryListDto sideBarCategoryListDto = categoryService.findSidebarCategory();
            List<RecentCommentResponse> sideBarRecentComment = commentService.findRecentComment();
            modelAndView.addObject("sideBarCategoryListDto", sideBarCategoryListDto);
            modelAndView.addObject("sideBarRecentComment", sideBarRecentComment);
        }
    }


}
