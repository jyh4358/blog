package com.myblog.comment.dto;

import com.myblog.comment.model.Comment;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ManageCommentResponse {
    private Long id;
    private String content;
    private Long articleId;
    private String articleTitle;
    private String username;
    private LocalDateTime createDate;

    public ManageCommentResponse(Long id,
                                 String content,
                                 Long articleId,
                                 String articleTitle,
                                 String username,
                                 LocalDateTime createDate) {
        this.id = id;
        this.content = content;
        this.articleId = articleId;
        this.articleTitle = articleTitle;
        this.username = username;
        this.createDate = createDate;
    }

    public static ManageCommentResponse of(Comment comment) {
        return new ManageCommentResponse(
                comment.getId(),
                comment.getContent(),
                comment.getArticle().getId(),
                comment.getArticle().getTitle(),
                comment.getMember().getUsername(),
                comment.getCreatedDate()
        );
    }
}
