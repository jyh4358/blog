package com.myblog.comment.controller;

import com.myblog.article.dto.PageDto;
import com.myblog.comment.dto.ManageCommentResponse;
import com.myblog.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/comments")
    public void findCommentList(@RequestParam Long articleId) {
        System.out.println("articleId = " + articleId);
    }

    @GetMapping("/admin/comments")
    public String findAllComment(
            @PageableDefault(size = 8, sort = "id",direction = Sort.Direction.DESC) Pageable pageable,
            Model model
    ) {
        Page<ManageCommentResponse> manageCommentList = commentService.findAllComment(pageable);
        model.addAttribute("manageCommentList", manageCommentList);
        model.addAttribute("pageDto", PageDto.of(manageCommentList));


        return "admin/comment/commentList";
    }
}
