package com.myblog.comment.controller;

import com.myblog.comment.dto.CommentListRequest;
import com.myblog.comment.dto.CommentListResponse;
import com.myblog.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentApiController {

    private final CommentService commentService;


    @GetMapping("/api/v1/article/{articleId}/comments")
    public ResponseEntity<List<CommentListResponse>> findComment(
            @PathVariable Long articleId
    ) {
        List<CommentListResponse> commentListResponses = commentService.findCommentList(articleId);
        System.out.println("commentListResponses = " + commentListResponses);
        return new ResponseEntity<>(commentListResponses, HttpStatus.OK);
    }
}
