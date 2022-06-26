package com.myblog.comment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class CommentController {

    @GetMapping("/comments")
    public void findCommentList(@RequestParam Long articleId) {
        System.out.println("articleId = " + articleId);
    }
}
