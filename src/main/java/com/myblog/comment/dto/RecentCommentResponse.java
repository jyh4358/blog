package com.myblog.comment.dto;

import com.myblog.comment.model.Comment;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecentCommentResponse {
    private Long articleId;
    private String content;
    private Long memberId;
    private String username;
    private boolean secret;

    public RecentCommentResponse(Long articleId, String content, Long memberId, String username, boolean secret) {
        this.articleId = articleId;
        this.content = content;
        this.memberId = memberId;
        this.username = username;
        this.secret = secret;
    }

    public static RecentCommentResponse of(Comment comment) {
        return new RecentCommentResponse(
                comment.getArticle().getId(),
                comment.getArticle().getContent(),
                comment.getMember().getId(),
                comment.getMember().getUsername(),
                comment.isSecret()
        );
    }
}
