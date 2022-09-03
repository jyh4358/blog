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

    /**
     *  게시물에 등록된 댓글 조회
     */
    @GetMapping("/api/v1/comments")
    public ResponseEntity<List<CommentListResponse>> findComment(
            @PathVariable Long articleId
    ) {
        List<CommentListResponse> commentListResponses = commentService.findCommentList(articleId);
        return new ResponseEntity<>(commentListResponses, HttpStatus.OK);
    }

    /**
     *  게시물에 댓글 등록
     */
    @PostMapping("/api/v1/comment")
    public ResponseEntity<Void> saveComment(
            @AuthenticationPrincipal CustomOauth2User customOauth2User,
            @Valid @RequestBody CommentSaveRequest commentSaveRequest
    ) {
        commentService.saveComment(customOauth2User, commentSaveRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    /**
     *  게시물
     */
    @DeleteMapping("/api/v1/comment/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @AuthenticationPrincipal CustomOauth2User customOauth2User,
            @PathVariable Long commentId
    ) {
        commentService.deleteComment(customOauth2User, commentId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
