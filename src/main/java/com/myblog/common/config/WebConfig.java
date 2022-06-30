package com.myblog.common.config;

import com.myblog.category.service.CategoryService;
import com.myblog.comment.service.CommentService;
import com.myblog.common.interceper.SideBarInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final CategoryService categoryService;
    private final CommentService commentService;


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SideBarInterceptor(categoryService, commentService))
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns("/css/**", "/images/**", "/*.ico", "/js/**", "/error", "/admin/**", "/file/**", "/article/temp/getTemp", "/api/**");
    }


}
