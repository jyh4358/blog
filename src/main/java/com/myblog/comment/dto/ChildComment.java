package com.myblog.comment.dto;

import com.myblog.comment.model.Comment;
import com.myblog.member.model.Member;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ChildComment {
    private Long id;
    private String content;
    private Long memberId;
    private String username;
    private String picUrl;
    private boolean secret;
    private LocalDateTime createDate;

    public static ChildComment of(Comment comment) {
        return new ChildComment(
                comment.getId(),
                comment.getContent(),
                comment.getMember().getId(),
                comment.getMember().getUsername(),
                comment.getMember().getPicUrl(),
                comment.isSecret(),
                comment.getCreatedDate()
        );
    }
}
