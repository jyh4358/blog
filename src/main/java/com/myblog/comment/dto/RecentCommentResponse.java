package com.myblog.comment.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
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

    public static RecentCommentResponse of(Long articleId, String content, Long memberId, String username, boolean secret) {
        return new RecentCommentResponse(
                articleId,
                content,
                memberId,
                username,
                secret
        );
    }
}
