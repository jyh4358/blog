package com.myblog.comment.controller;

import com.myblog.comment.dto.CommentListResponse;
import com.myblog.comment.dto.CommentSaveRequest;
import com.myblog.comment.service.CommentService;
import com.myblog.security.oauth2.model.CustomOauth2User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
        return new ResponseEntity<>(commentListResponses, HttpStatus.OK);
    }

    @PostMapping("/api/v1/comment")
    public ResponseEntity<Void> saveComment(
            @AuthenticationPrincipal CustomOauth2User customOauth2User,
            @Valid @RequestBody CommentSaveRequest commentSaveRequest
    ) {
        commentService.saveComment(customOauth2User, commentSaveRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // todo - 이후에 수정 구현(화면단 구현해야 됨)
//    @PatchMapping("/api/v1/comment/{commentId}")
//    public ResponseEntity<Void> modifyComment(
//            @AuthenticationPrincipal CustomOauth2User customOauth2User,
//            @PathVariable String commentId
//    ) {
//        System.out.println("commentId = " + commentId);
//        return new ResponseEntity<>(HttpStatus.OK);
//    }

    @DeleteMapping("/api/v1/comment/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @AuthenticationPrincipal CustomOauth2User customOauth2User,
            @PathVariable Long commentId
    ) {
        commentService.deleteComment(customOauth2User, commentId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
