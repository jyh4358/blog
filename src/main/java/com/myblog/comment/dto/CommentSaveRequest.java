package com.myblog.comment.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CommentSaveRequest {
    @NotNull(message = "게시글 아이디는 필수입니다.")
    private Long articleId;
    @NotBlank(message = "댓글 내용을 입력해주세요.")
    private String content;
    @NotNull(message = "비밀 값이 존재하지 않습니다.")
    private boolean secret;
    private Long parentCommentId;


    public boolean checkParentCommentId() {
        return parentCommentId != null;
    }
}
