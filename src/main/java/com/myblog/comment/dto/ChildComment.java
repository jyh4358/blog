package com.myblog.comment.dto;

import com.myblog.comment.model.Comment;
import com.myblog.member.model.Member;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ChildComment {
    private Long id;
    private String content;
    private String username;
    private String picUrl;
    private boolean secret;
    private LocalDateTime localDateTime;

    public static ChildComment of(Comment comment, Member member) {
        return new ChildComment(
                comment.getId(),
                comment.getContent(),
                member.getUsername(),
                member.getPicUrl(),
                comment.isSecret(),
                comment.getCreatedDate()
        );
    }
}
