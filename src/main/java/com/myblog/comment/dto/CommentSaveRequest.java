package com.myblog.comment.dto;

import lombok.*;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentSaveRequest {
    private Long articleId;
    private String content;
    private boolean secret;
    private Long parentCommentId;

    public CommentSaveRequest(Long articleId, String content, boolean secret, Long parentCommentId) {
        this.articleId = articleId;
        this.content = content;
        this.secret = secret;
        this.parentCommentId = parentCommentId;
    }

    public boolean checkParentCommentId() {
        return parentCommentId != null;
    }
}
