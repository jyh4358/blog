package com.myblog.comment.dto;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CommentListRequest {
    private Long articleId;
}
